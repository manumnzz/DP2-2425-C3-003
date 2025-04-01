
package acme.features.airlinemanager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Airport;
import acme.entities.S1.Flight;
import acme.entities.S1.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightShowService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;
		AirlineManager airlineManager;

		masterId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(masterId);
		airlineManager = flight == null ? null : flight.getAirlineManager();
		status = super.getRequest().getPrincipal().hasRealm(airlineManager) || flight != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Collection<Airport> airports;
		Collection<Leg> legs;
		SelectChoices choices1;
		SelectChoices choices2;
		SelectChoices choices3;
		SelectChoices choices4;
		Dataset dataset;

		// Obtener listas de aeropuertos y legs
		airports = this.repository.findAllAirports();
		legs = this.repository.findAllPublishedLegs();

		// Crear listas desplegables
		choices1 = SelectChoices.from(airports, "name", flight.getOriginAirport());
		choices2 = SelectChoices.from(airports, "name", flight.getDestinationAirport());
		choices3 = SelectChoices.from(legs, "flightNumber", flight.getFirstLeg());
		choices4 = SelectChoices.from(legs, "flightNumber", flight.getLastLeg());

		// Unbind de los atributos básicos del vuelo
		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");

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
