
package acme.features.assistanceagent.claim;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.S1.Leg;
import acme.entities.S4.Claim;
import acme.entities.S4.TrackingLog;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.id = :claimId")
	Claim findClaimById(int claimId);

	@Query("select a.id from AssistanceAgent a where a.userAccount.id = :userAccountId")
	int findAssistanceAgentIdByUserAccountId(int userAccountId);

	@Query("select a from AssistanceAgent a where a.id = :id")
	AssistanceAgent findAssistanceAgentById(int id);

	@Query("select c.assistanceAgent.id from Claim c where c.id = :claimId")
	int findAssistanceAgentIdByClaimId(int claimId);

	@Query("select a from AssistanceAgent a where a.userAccount.id = :userAccountId")
	AssistanceAgent findAssistanceAgentByUserAccountId(int userAccountId);

	@Query("select l from Leg l where l.draftMode = false and l.aircraft.airline.id = :airlineId")
	Collection<Leg> findAllPublishedLegs(int airlineId);

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select c from Claim c where c.assistanceAgent.id = :assistanceAgentId")
	Collection<Claim> findClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("select tr from TrackingLog tr where tr.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);

	@Query("select distinct tr.claim from TrackingLog tr where tr.resolutionPercentage = 100.0 and tr.claim.assistanceAgent.id = :assistanceAgentId and tr.draftMode = false")
	List<Claim> findClaimsCompleted(int assistanceAgentId);

	@Query("SELECT c FROM Claim c WHERE c.assistanceAgent.id = :assistanceAgentId AND NOT EXISTS (SELECT tr FROM TrackingLog tr WHERE tr.claim = c AND tr.resolutionPercentage = 100.0 AND tr.draftMode = false)")
	List<Claim> findClaimsUndergoing(int assistanceAgentId);

}
