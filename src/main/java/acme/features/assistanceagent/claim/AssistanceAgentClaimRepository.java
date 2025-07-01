
package acme.features.assistanceagent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.S1.Leg;
import acme.entities.S4.Claim;
import acme.entities.S4.TrackingLog;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("SELECT c from Claim c where c.id = :id")
	Claim findClaimById(int id);

	@Query("SELECT c from Claim c where c.assistanceAgent.id = :assistanceAgentId")
	Collection<Claim> findClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLeg();

	@Query("SELECT t from TrackingLog t where t.claim.id = :id")
	Collection<TrackingLog> findAllTrackingLogsByClaimId(int id);

	@Query("SELECT a FROM AssistanceAgent a where a.id = :id")
	AssistanceAgent findAssistanceAgentById(int id);

	@Query("select l from Leg l where l.draftMode = false and l.aircraft.airline.id = :airlineId")
	Collection<Leg> findAllPublishedLegs(int airlineId);

	@Query("SELECT l FROM Leg l where l.id = :id")
	Leg findLegById(int id);

}
