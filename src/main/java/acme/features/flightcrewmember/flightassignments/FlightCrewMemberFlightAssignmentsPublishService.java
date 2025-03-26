
package acme.features.flightcrewmember.flightassignments;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.entities.S3.CurrentStatus;
import acme.entities.S3.FlightAssignment;
import acme.entities.S3.FlightCrew;
import acme.realms.FlightCrewMember;

public class FlightCrewMemberFlightAssignmentsPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentsRepository repository;

	// AbstractService<AirlineManager, Flight> -------------------------------------


	@Override
	public void authorise() {
		boolean isLeadAttendant;
		FlightAssignment flightAssignment;

		flightAssignment = this.repository.findFlightAssignmentById(super.getRequest().getPrincipal().getAccountId());
		isLeadAttendant = flightAssignment != null && flightAssignment.getFlightCrew() == FlightCrew.LEAD_ATTENDANT;

		super.getResponse().setAuthorised(isLeadAttendant);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		super.bindObject(flightAssignment, "flightCrew", "currentStatus", "remarks");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		Date moment;
		moment = MomentHelper.getCurrentMoment();
		flightAssignment.setMoment(moment);
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		SelectChoices choices1;
		SelectChoices choices2;
		Dataset dataset;

		choices1 = SelectChoices.from(FlightCrew.class, flightAssignment.getFlightCrew());
		choices2 = SelectChoices.from(CurrentStatus.class, flightAssignment.getCurrentStatus());

		dataset = super.unbindObject(flightAssignment, "flightCrew", "currentStatus", "remarks");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("flightCrews", choices1);
		dataset.put("currentStatuses", choices2);

		super.getResponse().addData(dataset);
	}
}
