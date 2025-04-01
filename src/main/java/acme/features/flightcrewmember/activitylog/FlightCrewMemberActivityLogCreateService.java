
package acme.features.flightcrewmember.activitylog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S3.ActivityLog;
import acme.entities.S3.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractService<AirlineManager, Flight> -------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		FlightCrewMember flightCrewMember;
		FlightAssignment flightAssignment;
		int flightAssignmentId;
		Date moment;

		flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		flightAssignmentId = super.getRequest().getData("flightAssignmentId", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
		moment = MomentHelper.getCurrentMoment();

		activityLog = new ActivityLog();
		activityLog.setMoment(moment);
		activityLog.setIncident("");
		activityLog.setDescription("");
		activityLog.setSeverityLevel(0);
		activityLog.setCrewMember(flightCrewMember);
		activityLog.setFlightAssignment(flightAssignment);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		super.unbindObject(activityLog, "moment", "incident", "description", "severityLevel", "crewMember", "flightAssignment");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		Date moment;
		moment = MomentHelper.getCurrentMoment();
		activityLog.setMoment(moment);
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		dataset = super.unbindObject(activityLog, "moment", "incident", "description", "severityLevel", "crewMember", "flightAssignment");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}
}
