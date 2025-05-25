
package acme.features.flightcrewmember.activitylog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S3.ActivityLog;
import acme.entities.S3.FlightAssignment;
import acme.realms.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogShowService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractService<AirlineManager, Flight> -------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
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
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;
		FlightAssignment flightAssignment;
		FlightCrewMember crewMember;

		flightAssignment = activityLog.getFlightAssignment();
		crewMember = activityLog.getFlightCrewMember();

		dataset = super.unbindObject(activityLog, "flightCrew", "currentStatus", "remarks");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		dataset.put("flightAssignmentFlightCrew", flightAssignment.getFlightCrew());
		dataset.put("flightAssignmentMoment", flightAssignment.getMoment());
		dataset.put("flightAssignmentCurrentStatuts", flightAssignment.getCurrentStatus());
		dataset.put("flightAssignmentRemarks", flightAssignment.getRemarks());

		dataset.put("crewMemberEmployeCode", crewMember.getEmployeeCode());
		dataset.put("crewMemberPhone", crewMember.getPhone());
		dataset.put("crewMemberLanguage", crewMember.getLanguage());
		dataset.put("crewMemberAvailability", crewMember.getAvailability());
		dataset.put("crewMemberSalary", crewMember.getSalary());
		dataset.put("crewMemberExperience", crewMember.getExperience());

		super.getResponse().addData(dataset);
	}
}
