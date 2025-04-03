
package acme.features.airlinemanager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Airport;
import acme.entities.S1.Flight;
import acme.entities.S1.FlightLeg;
import acme.entities.S1.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightCreateService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightRepository repository;

	// AbstractService<AirlineManager, Flight> -------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Flight flight;
		AirlineManager airlineManager;

		airlineManager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();

		flight = new Flight();
		flight.setDraftMode(true);
		flight.setAirlineManager(airlineManager);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		int firstLegId;
		int lastLegId;
		int originAirportId;
		int destinationAirportId;
		int airlineManagerId;

		Leg firstLeg;
		Leg lastLeg;
		Airport originAirport;
		Airport destinationAirport;
		AirlineManager airlineManager;

		firstLegId = super.getRequest().getData("firstLeg", int.class);
		lastLegId = super.getRequest().getData("lastLeg", int.class);
		originAirportId = super.getRequest().getData("originAirport", int.class);
		destinationAirportId = super.getRequest().getData("destinationAirport", int.class);
		airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		firstLeg = this.repository.findLegById(firstLegId);
		lastLeg = this.repository.findLegById(lastLegId);
		originAirport = this.repository.findAirportById(originAirportId);
		destinationAirport = this.repository.findAirportById(destinationAirportId);
		airlineManager = this.repository.findAirlineManagerById(airlineManagerId);

		super.bindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
		flight.setFirstLeg(firstLeg);
		flight.setLastLeg(lastLeg);
		flight.setOriginAirport(originAirport);
		flight.setDestinationAirport(destinationAirport);
		flight.setAirlineManager(airlineManager);

	}

	@Override
	public void validate(final Flight flight) {
		;
	}

	@Override
	public void perform(final Flight flight) {
		// 1️⃣ Guardar el vuelo en la BD
		this.repository.save(flight);

		// 2️⃣ Crear las relaciones en FlightLeg
		FlightLeg firstFlightLeg = new FlightLeg();
		firstFlightLeg.setFlight(flight);
		firstFlightLeg.setLeg(flight.getFirstLeg());
		firstFlightLeg.setSequence(1); // Primera escala
		this.repository.save(firstFlightLeg);

		FlightLeg lastFlightLeg = new FlightLeg();
		lastFlightLeg.setFlight(flight);
		lastFlightLeg.setLeg(flight.getLastLeg());
		lastFlightLeg.setSequence(999); // Última escala (ajusta según lógica)
		this.repository.save(lastFlightLeg);
	}

	@Override
	public void unbind(final Flight flight) {
		int airlineManagerId;
		Collection<Airport> airports;
		Collection<Leg> legs;
		SelectChoices choices1;
		SelectChoices choices2;
		SelectChoices choices3;
		SelectChoices choices4;
		Dataset dataset;

		airlineManagerId = flight.getAirlineManager().getId();

		// Obtener listas de aeropuertos y legs
		airports = this.repository.findAllAirports();
		legs = this.repository.findAllPublishedLegs(airlineManagerId);

		// Crear listas desplegables
		choices1 = SelectChoices.from(airports, "name", flight.getOriginAirport());
		choices2 = SelectChoices.from(airports, "name", flight.getDestinationAirport());
		choices3 = SelectChoices.from(legs, "flightNumber", flight.getFirstLeg());
		choices4 = SelectChoices.from(legs, "flightNumber", flight.getLastLeg());

		// Unbind de los atributos básicos del vuelo
		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode");

		// Agregar aeropuertos y legs al dataset
		dataset.put("originAirport", choices1.getSelected().getKey());
		dataset.put("destinationAirport", choices2.getSelected().getKey());
		dataset.put("originAirports", choices1);
		dataset.put("destinationAirports", choices2);

		dataset.put("firstLeg", choices3.getSelected().getKey());
		dataset.put("lastLeg", choices4.getSelected().getKey());
		dataset.put("firstLegs", choices3);
		dataset.put("lastLegs", choices4);

		super.getResponse().addData(dataset);
	}

}
