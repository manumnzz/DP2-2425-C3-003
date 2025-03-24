
package acme.features.airlinemanager.flights;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S1.Flight;
import acme.entities.S1.FlightLeg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightDeleteService extends AbstractGuiService<AirlineManager, Flight> {

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
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "firstLeg", "lastLeg", "originAirport", "destinationAirport", "airlineManager");
	}

	@Override
	public void validate(final Flight flight) {
		;
	}

	@Override
	public void perform(final Flight flight) {
		Collection<FlightLeg> flightlegs;

		flightlegs = this.repository.findLegsByFlightId(flight.getId());
		this.repository.deleteAll(flightlegs);
		this.repository.delete(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "firstLeg", "lastLeg", "originAirport", "destinationAirport", "airlineManager");

	}
}
