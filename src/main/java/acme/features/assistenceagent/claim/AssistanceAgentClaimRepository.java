
package acme.features.assistenceagent.claim;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.S4.Claim;
import acme.entities.S4.TrackingLog;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("SELECT c FROM Claim c WHERE c.assistenceAgent.id = ?1")
	Collection<Claim> findClaimsByAssisntenceAgentId(int id);

	@Query("SELECT c FROM Claim c WHERE c.id = ?1")
	Claim findClaimById(int id);

	@Query("SELECT c FROM  Claim c")
	List<Claim> findAllClaims();

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = ?1")
	Collection<TrackingLog> findTrackingLogByClaimId(int id);

}
