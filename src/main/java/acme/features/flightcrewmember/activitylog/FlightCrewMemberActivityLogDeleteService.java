
package acme.features.flightcrewmember.activitylog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S3.ActivityLog;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogDeleteService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

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
		int id;

		id = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(id);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		super.unbindObject(activityLog, "moment", "incident", "description", "severityLevel", "flightCrewMember", "flightAssignment");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(activityLog.isDraftMode(), "draftMode", "flightcrewmemberactivityLog.error.draftMode");
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		Date moment;
		moment = MomentHelper.getCurrentMoment();
		activityLog.setMoment(moment);
		this.repository.delete(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {

		Dataset dataset;

		dataset = super.unbindObject(activityLog, "moment", "incident", "description", "severityLevel", "flightCrewMember", "flightAssignment");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}
}
