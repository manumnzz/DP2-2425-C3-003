
package acme.features.airlinemanager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S1.Flight;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightListService extends AbstractGuiService<AirlineManager, Flight> {

	//Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightRepository repository;

	//AbstractGuiService interface ---------------------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Flight> flights;
		int id;

		id = super.getRequest().getPrincipal().getActiveRealm().getId();
		flights = this.repository.findFlightsByAirlineManagerId(id);

		super.getBuffer().addData(flights);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		// Unbind de los atributos normales
		dataset = super.unbindObject(flight, "tag", "cost", "originCity", "destinationCity", "scheduledDeparture", "scheduledArrival");
		super.addPayload(dataset, flight, "requiresSelfTransfer", "description", "originAirport.name", "destinationAirport.name", "firstLeg", "lastLeg", "airlineManager");

		// Añadir la respuesta con los datos al dataset
		super.getResponse().addData(dataset);
	}

}
