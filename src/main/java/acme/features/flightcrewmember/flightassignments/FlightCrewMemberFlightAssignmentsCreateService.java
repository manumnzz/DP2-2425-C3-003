
package acme.features.flightcrewmember.flightassignments;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S1.Leg;
import acme.entities.S3.AvailabilityStatus;
import acme.entities.S3.CurrentStatus;
import acme.entities.S3.FlightAssignment;
import acme.entities.S3.FlightCrew;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentsCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentsRepository repository;

	// AbstractService<AirlineManager, Flight> -------------------------------------


	@Override
	public void authorise() {
		//		boolean isLeadAttendant;
		//		FlightAssignment flightAssignment;
		//
		//		flightAssignment = this.repository.findFlightAssignmentById(super.getRequest().getPrincipal().getAccountId());
		//		isLeadAttendant = flightAssignment != null && flightAssignment.getFlightCrew() == FlightCrew.LEAD_ATTENDANT;
		//
		//		super.getResponse().setAuthorised(isLeadAttendant);
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		//		FlightAssignment flightAssignment;
		//		FlightCrewMember flightCrewMember;
		//		int legId;
		//		Leg leg;
		//		Date moment;
		//
		//		flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		//		legId = super.getRequest().getData("legId", int.class);
		//		leg = this.repository.findLegById(legId);
		//		moment = MomentHelper.getCurrentMoment();
		//
		//		flightAssignment = new FlightAssignment();
		//		flightAssignment.setMoment(moment);
		//		flightAssignment.setCurrentStatus(CurrentStatus.CONFIRMED);
		//		flightAssignment.setRemarks("");
		//		flightAssignment.setFlightCrewMember(flightCrewMember);
		//		flightAssignment.setLeg(leg);
		//
		//		super.getBuffer().addData(flightAssignment);
		FlightAssignment flightAssignment;
		FlightCrewMember flightCrewMember;

		flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		flightAssignment = new FlightAssignment();
		flightAssignment.setFlightCrewMember(flightCrewMember);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		int legId;

		Leg leg;

		legId = this.getRequest().getData("leg", int.class);

		leg = this.repository.findLegById(legId);

		flightAssignment.setLeg(leg);
		super.bindObject(flightAssignment, "flightCrew", "moment", "currentStatus", "remarks");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		int legId = flightAssignment.getLeg().getId();
		int pilots = this.repository.countPilotsByFlightAssignmentId(legId);
		int copilots = this.repository.countCopilotsByFlightAssignmentId(legId);
		FlightCrewMember crewMember = flightAssignment.getFlightCrewMember();
		int activeAssignments = this.repository.countActiveAssignmentsByMemberId(crewMember.getId());

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
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		SelectChoices choices1;
		SelectChoices choices2;
		SelectChoices choices3;
		Collection<Leg> legs;
		Dataset dataset;

		legs = this.repository.findAllLegs();

		choices1 = SelectChoices.from(FlightCrew.class, flightAssignment.getFlightCrew());
		choices2 = SelectChoices.from(CurrentStatus.class, flightAssignment.getCurrentStatus());
		choices3 = SelectChoices.from(legs, "flightNumber", flightAssignment.getLeg());

		dataset = super.unbindObject(flightAssignment, "flightCrew", "moment", "currentStatus", "remarks");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("flightCrews", choices1);
		dataset.put("currentStatuses", choices2);
		dataset.put("legs", choices3.getSelected().getKey());
		dataset.put("legs", choices3);

		super.getResponse().addData(dataset);
	}
}
