
package acme.features.airlinemanager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Airport;
import acme.entities.S1.Flight;
import acme.entities.S1.FlightLeg;
import acme.entities.S1.FlightStatus;
import acme.entities.S1.Leg;
import acme.entities.aircraft.Aircraft;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegCreateService extends AbstractGuiService<AirlineManager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerLegRepository repository;

	// AbstractService<AirlineManager, Leg> -------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Leg leg;
		AirlineManager airlineManager;

		airlineManager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();

		leg = new Leg();
		leg.setDraftMode(true);
		leg.setAirlineManager(airlineManager);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		int departureAirportId;
		int arrivalAirportId;
		int aircraftId;
		int airlineManagerId;

		Airport departureAirport;
		Airport arrivalAirport;
		Aircraft aircraft;
		AirlineManager airlineManager;

		departureAirportId = this.getRequest().getData("departureAirport", int.class);
		arrivalAirportId = this.getRequest().getData("arrivalAirport", int.class);
		aircraftId = this.getRequest().getData("aircraft", int.class);
		airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		departureAirport = this.repository.findAirportById(departureAirportId);
		arrivalAirport = this.repository.findAirportById(arrivalAirportId);
		aircraft = this.repository.findAircraftById(aircraftId);
		airlineManager = this.repository.findAirlineManagerById(airlineManagerId);

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");
		leg.setDepartureAirport(departureAirport);
		leg.setArrivalAirport(arrivalAirport);
		leg.setAircraft(aircraft);
		leg.setAirlineManager(airlineManager);
	}

	@Override
	public void validate(final Leg leg) {
		;
	}

	@Override
	public void perform(final Leg leg) {
		// Guardar el nuevo Leg en la base de datos
		this.repository.save(leg);

		// Buscar el Flight al que pertenece este Leg
		Flight flight = this.repository.findFlightByLegId(leg.getId());

		// Si encontramos el vuelo, creamos la relación FlightLeg
		if (flight != null) {
			FlightLeg flightLeg = new FlightLeg();
			flightLeg.setFlight(flight);
			flightLeg.setLeg(leg);
			flightLeg.setSequence(this.calcularSequence(flight)); // Método para determinar la secuencia

			this.repository.save(flightLeg);
		}
	}

	private int calcularSequence(final Flight flight) {
		Integer maxSequence = this.repository.findMaxSequenceByFlightId(flight.getId());
		return maxSequence == null ? 1 : maxSequence + 1;
	}

	@Override
	public void unbind(final Leg leg) {
		Collection<Airport> airports;
		Collection<Aircraft> aircrafts;
		SelectChoices choices1;
		SelectChoices choices2;
		SelectChoices choices3;
		SelectChoices choices4;
		Dataset dataset;

		// Obtener listas de aeropuertos y aeronaves
		airports = this.repository.findAllAirports();
		aircrafts = this.repository.findAllAircrafts();

		// Crear listas desplegables
		choices1 = SelectChoices.from(airports, "name", leg.getDepartureAirport());
		choices2 = SelectChoices.from(airports, "name", leg.getArrivalAirport());
		choices3 = SelectChoices.from(aircrafts, "model", leg.getAircraft());
		choices4 = SelectChoices.from(FlightStatus.class, leg.getStatus());

		// Unbind de los atributos básicos de la Leg
		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "draftMode");

		// Agregar valores seleccionados al dataset
		dataset.put("departureAirport", choices1.getSelected().getKey());
		dataset.put("arrivalAirport", choices2.getSelected().getKey());
		dataset.put("aircraft", choices3.getSelected().getKey());

		// Agregar listas completas de opciones al dataset
		dataset.put("departureAirports", choices1);
		dataset.put("arrivalAirports", choices2);
		dataset.put("aircrafts", choices3);
		dataset.put("statuses", choices4);

		// Enviar el dataset a la respuesta
		super.getResponse().addData(dataset);
	}

}
