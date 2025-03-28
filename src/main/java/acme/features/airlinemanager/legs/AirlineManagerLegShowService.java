
package acme.features.airlinemanager.legs;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Airport;
import acme.entities.S1.Leg;
import acme.entities.aircraft.Aircraft;
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
	}

	@Override
	public void unbind(final Leg leg) {
		int airlineManagerId;
		Collection<Airport> airports;
		Collection<Aircraft> aircrafts;
		SelectChoices departureChoices;
		SelectChoices arrivalChoices;
		SelectChoices aircraftChoices;
		Dataset dataset;

		airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		// Obtener listas de aeropuertos y aeronaves
		airports = this.repository.findAllAirports();
		aircrafts = this.repository.findAllAircrafts();

		// Crear listas desplegables
		departureChoices = SelectChoices.from(airports, "name", leg.getDepartureAirport());
		arrivalChoices = SelectChoices.from(airports, "name", leg.getArrivalAirport());
		aircraftChoices = SelectChoices.from(aircrafts, "identifier", leg.getAircraft());

		// Unbind de los atributos básicos de Leg
		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");

		// Agregar aeropuertos y aeronaves al dataset
		dataset.put("departureAirport", departureChoices.getSelected().getKey());
		dataset.put("arrivalAirport", arrivalChoices.getSelected().getKey());
		dataset.put("departureAirports", departureChoices);
		dataset.put("arrivalAirports", arrivalChoices);

		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);

		// Enviar los datos a la respuesta
		super.getResponse().addData(dataset);
	}

}
