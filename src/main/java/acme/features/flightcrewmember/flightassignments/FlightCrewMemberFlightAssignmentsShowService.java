
package acme.features.flightcrewmember.flightassignments;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.entities.S1.Leg;
import acme.entities.S3.CurrentStatus;
import acme.entities.S3.FlightAssignment;
import acme.entities.S3.FlightCrew;
import acme.realms.FlightCrewMember;

public class FlightCrewMemberFlightAssignmentsShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentsRepository repository;

	// AbstractService<AirlineManager, Flight> -------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		FlightAssignment flightAssignment;
		FlightCrewMember flightCrewMember;

		masterId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);
		flightCrewMember = flightAssignment == null ? null : flightAssignment.getFlightCrewMember();
		status = super.getRequest().getPrincipal().hasRealm(flightCrewMember) || flightAssignment != null;

		super.getResponse().setAuthorised(status);
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
	public void unbind(final FlightAssignment flightAssignment) {
		SelectChoices choices1;
		SelectChoices choices2;
		Dataset dataset;
		Leg leg;
		FlightCrewMember crewMember;

		choices1 = SelectChoices.from(FlightCrew.class, flightAssignment.getFlightCrew());
		choices2 = SelectChoices.from(CurrentStatus.class, flightAssignment.getCurrentStatus());

		leg = flightAssignment.getLeg();
		crewMember = flightAssignment.getFlightCrewMember();

		dataset = super.unbindObject(flightAssignment, "flightCrew", "currentStatus", "remarks");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("flightCrews", choices1);
		dataset.put("currentStatuses", choices2);

		dataset.put("legOrigin", leg.getFlightNumber());
		dataset.put("legScheduledDeparture", leg.getScheduledDeparture());
		dataset.put("legScheduledArrival", leg.getScheduledArrival());
		dataset.put("legStatus", leg.getStatus());

		dataset.put("crewMemberEmployeCode", crewMember.getEmployeeCode());
		dataset.put("crewMemberPhone", crewMember.getPhone());
		dataset.put("crewMemberLanguage", crewMember.getLanguage());
		dataset.put("crewMemberAvailability", crewMember.getAvailability());
		dataset.put("crewMemberSalary", crewMember.getSalary());
		dataset.put("crewMemberExperience", crewMember.getExperience());

		super.getResponse().addData(dataset);
	}
}
