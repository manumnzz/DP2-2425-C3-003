
package acme.features.flightcrewmember.flightassignments;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.S1.Leg;
import acme.entities.S3.CurrentStatus;
import acme.entities.S3.FlightAssignment;

@Repository
public interface FlightCrewMemberFlightAssignmentsRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :id")
	Collection<FlightAssignment> findFlightAssignmentsByCrewMemberId(@Param("id") int id);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :id and fa.leg.status = ON_TIME")
	Collection<FlightAssignment> findPlannedAssignmentsByCrewMemberId(int id);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :id and fa.leg.status = LANDED")
	Collection<FlightAssignment> findCompletedAssignmentsByCrewMemberId(int id);

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("SELECT CASE WHEN COUNT(fa) > 0 THEN TRUE ELSE FALSE END " + "FROM FlightAssignment fa " + "WHERE fa.flightCrewMember.id = :memberId AND fa.flightCrew = 'LEAD_ATTENDANT'")
	boolean isLeadAttendant(@Param("memberId") int memberId);

	@Query("select count(fa) from FlightAssignment fa where fa.id = :id and fa.flightCrew = 'PILOT'")
	int countPilotsByFlightAssignmentId(int id);

	@Query("select count(fa) from FlightAssignment fa where fa.id = :id and fa.flightCrew = 'COPILOT'")
	int countCopilotsByFlightAssignmentId(int id);

	@Query("select case when l.status = LANDED then true else false end from Leg l where l.id = :legId")
	boolean isLegCompleted(int legId);

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select count(fa) from FlightAssignment fa where fa.flightCrewMember.id = :memberId AND fa.leg.status IN (acme.entities.S1.FlightStatus.ON_TIME, acme.entities.S1.FlightStatus.DELAYED)")
	int countActiveAssignmentsByMemberId(int memberId);

	@Query("SELECT fa.currentStatus FROM FlightAssignment fa WHERE fa.id = :id")
	CurrentStatus getFlightAssignmentStatus(@Param("id") int id);

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLegs();
}
