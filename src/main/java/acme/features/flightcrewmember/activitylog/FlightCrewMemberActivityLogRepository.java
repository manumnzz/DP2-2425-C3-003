
package acme.features.flightcrewmember.activitylog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.S3.ActivityLog;
import acme.entities.S3.FlightAssignment;

@Repository
public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select al from ActivityLog al where al.flightCrewMember.id = :id")
	Collection<ActivityLog> findActivityLogByCrewMemberId(@Param("id") int id);

	@Query("select al from ActivityLog al where al.flightAssignment.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select al from ActivityLog al where al.id = :id")
	ActivityLog findActivityLogById(int id);

}
