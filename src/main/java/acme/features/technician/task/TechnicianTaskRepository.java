
package acme.features.technician.task;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.Task;
import acme.realms.Technician;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("select t from Technician t where t.userAccount.id = :id")
	Technician findTechnicianById(int id);

	@Query("select t from Task t where t.technician.userAccount.id = :id")
	Collection<Task> findTaskByTechnicianId(int id);

	@Query("select t from Task t")
	Collection<Task> findAllt(int id);

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAricraft();

	@Query("select t from Task t where t.id = :id")
	Task findTaskById(int id);

	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findMrById(int id);

	@Query("select mr from MaintenanceRecord mr where mr.draftMode = false")
	List<MaintenanceRecord> getAllMr();

}
