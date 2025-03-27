
package acme.features.flightcrewmember.flightassignments;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S3.AvailabilityStatus;
import acme.entities.S3.CurrentStatus;
import acme.entities.S3.FlightAssignment;
import acme.entities.S3.FlightCrew;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentsDeleteService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentsRepository repository;

	// AbstractService<AirlineManager, Flight> -------------------------------------


	@Override
	public void authorise() {
		boolean isLeadAttendant;
		FlightAssignment flightAssignment;
		int id = super.getRequest().getData("id", int.class);

		flightAssignment = this.repository.findFlightAssignmentById(super.getRequest().getPrincipal().getAccountId());
		isLeadAttendant = flightAssignment != null && flightAssignment.getFlightCrew() == FlightCrew.LEAD_ATTENDANT;

		CurrentStatus status = this.repository.getFlightAssignmentStatus(id);
		boolean isNotConfirmed = status != CurrentStatus.CONFIRMED;

		super.getResponse().setAuthorised(isLeadAttendant && isNotConfirmed);
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
		int legId = flightAssignment.getLeg().getId();
		int pilots = this.repository.countPilotsByFlightAssignmentId(legId);
		int copilots = this.repository.countCopilotsByFlightAssignmentId(legId);
		FlightCrewMember crewMember = flightAssignment.getFlightCrewMember();
		int activeAssignments = this.repository.countActiveAssignmentsByMemberId(crewMember.getId());

		super.state(flightAssignment.getCurrentStatus() != CurrentStatus.CONFIRMED, "*", "flightAssignment.error.alreadyConfirmed");

		super.state(activeAssignments == 0, "flightCrewMember", "flightAssignment.crewMember.alreadyAssigned");

		super.state(crewMember.getAvailability() == AvailabilityStatus.AVAILABLE, "flightCrewMember", "flightAssignment.crewMember.notAvailable");

		if (flightAssignment.getFlightCrew() == FlightCrew.PILOT)
			super.state(pilots < 1, "role", "flightAssignment.tooManyPilots");

		if (flightAssignment.getFlightCrew() == FlightCrew.COPILOT)
			super.state(copilots < 1, "role", "flightAssignment.tooManyCopilots");
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		Date moment;
		moment = MomentHelper.getCurrentMoment();
		flightAssignment.setMoment(moment);
		this.repository.delete(flightAssignment);
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
