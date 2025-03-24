
package acme.features.airlinemanager.flights;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S1.Flight;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightUpdateService extends AbstractGuiService<AirlineManager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightRepository repository;

	// AbstractService<AirlineManager, Flight> -------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;
		AirlineManager airlineManager;

		masterId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(masterId);
		airlineManager = flight == null ? null : flight.getAirlineManager();
		status = flight != null && super.getRequest().getPrincipal().hasRealm(airlineManager);

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
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "firstLeg", "lastLeg", "originAirport", "destinationAirport", "airlineManager");
	}

	@Override
	public void validate(final Flight flight) {
		;
	}

	@Override
	public void perform(final Flight flight) {
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "firstLeg", "lastLeg", "originAirport", "destinationAirport", "airlineManager");
	}
}
