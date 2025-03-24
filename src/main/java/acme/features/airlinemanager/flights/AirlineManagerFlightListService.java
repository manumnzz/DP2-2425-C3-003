
package acme.features.airlinemanager.flights;

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

		id = super.getRequest().getPrincipal().getAccountId();
		flights = this.repository.findFlightsByAirlineManagerId(id);

		super.getBuffer().addData(flights);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "firstLeg", "lastLeg", "originAirport", "destinationAirport", "airlineManager");
		super.addPayload(dataset, flight, "text", "moreInfo");

		super.getResponse().addData(dataset);
	}
}
