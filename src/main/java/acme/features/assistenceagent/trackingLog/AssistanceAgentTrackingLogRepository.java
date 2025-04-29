
package acme.features.assistenceagent.trackingLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.S4.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t WHERE t.assistenceAgent.id = ?1")
	Collection<TrackingLog> findTrackingLogByAssisntenceAgentId(int id);

	@Query("SELECT t FROM TrackingLog t WHERE t.id = ?1")
	TrackingLog findTrackingLogById(int id);
}
