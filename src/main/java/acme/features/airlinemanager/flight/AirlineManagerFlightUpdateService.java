
package acme.features.airlinemanager.flight;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S1.Flight;
import acme.entities.S1.FlightSelfTransfer;
import acme.entities.S1.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightUpdateService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repository;


	@Override
	public void authorise() {
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("id", int.class);

		flight = this.repository.findFlightById(flightId);

		boolean status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class) && super.getRequest().getPrincipal().getAccountId() == flight.getAirlineManager().getUserAccount().getId();

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
		super.bindObject(flight, "tag", "selfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		if (flight == null) {
			super.state(false, "*", "javax.validation.constraints.NotNull.message");
			return;
		}
		List<Leg> legs = this.repository.findByFlightIdOrdered(flight.getId());

		if (legs != null)
			for (int i = 0; i < legs.size() - 1; i++) {
				Leg currentLeg = legs.get(i);
				Leg nextLeg = legs.get(i + 1);

				boolean isInOrder = !MomentHelper.isAfter(currentLeg.getScheduledArrival(), nextLeg.getScheduledDeparture());

				super.state(isInOrder, "*", "acme.validation.flight.legs.sequential-order");
			}
	}

	@Override
	public void perform(final Flight flight) {
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		SelectChoices selfTransfers = SelectChoices.from(FlightSelfTransfer.class, flight.getSelfTransfer());

		dataset = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "draftMode");

		dataset.put("selfTransfers", selfTransfers);
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("originCity", flight.getOriginCity());
		dataset.put("destinationCity", flight.getDestinationCity());
		dataset.put("numberOfLayovers", flight.getNumberOfLayovers());

		super.getResponse().addData(dataset);
	}

}
