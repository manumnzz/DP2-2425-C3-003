
package acme.features.flightcrewmember.flightassignments;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.S1.Leg;
import acme.entities.S3.CurrentStatus;
import acme.entities.S3.FlightAssignment;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssignmentsRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :id")
	Collection<FlightAssignment> findFlightAssignmentsByCrewMemberId(int id);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :id and fa.completed = false")
	Collection<FlightAssignment> findPlannedAssignmentsByCrewMemberId(int id);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :id and fa.completed = true")
	Collection<FlightAssignment> findCompletedAssignmentsByCrewMemberId(int id);

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select count(fcm) from FlightCrewMember fcm where fcm.flightAssignment.id = :assignmentId and fcm.role = 'PILOT'")
	int countPilotsByFlightAssignmentId(int assignmentId);

	@Query("select count(fcm) from FlightCrewMember fcm where fcm.flightAssignment.id = :assignmentId and fcm.role = 'COPILOT'")
	int countCopilotsByFlightAssignmentId(int assignmentId);


	@Query("select case when l.status = LANDED then true else false end from Leg l where l.id = :legId")
	boolean isLegCompleted(int legId);

	@Query("select fa from FlightAssignment fa where fa.leg.id = :id")
	Leg findLegById(int id);

	@Query("select count(fa) from FlightAssignment fa where fa.flightCrewMember.id = :memberId AND fa.leg.status IN (acme.entities.S1.FlightStatus.ON_TIME, acme.entities.S1.FlightStatus.DELAYED)")
	int countActiveAssignmentsByMemberId(int memberId);

	@Query("SELECT fa.currentStatus FROM FlightAssignment fa WHERE fa.id = :id")
	CurrentStatus getFlightAssignmentStatus(@Param("id") int id);
}
