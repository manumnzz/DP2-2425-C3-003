
package acme.features.airlinemanager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Aircraft;
import acme.entities.Airport;
import acme.entities.S1.FlightStatus;
import acme.entities.S1.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegShowService extends AbstractGuiService<AirlineManager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Leg leg;
		AirlineManager airlineManager;

		masterId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(masterId);
		airlineManager = leg == null ? null : leg.getAirlineManager();
		status = super.getRequest().getPrincipal().hasRealm(airlineManager) || leg != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
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
		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "draftMode", "duration");

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
