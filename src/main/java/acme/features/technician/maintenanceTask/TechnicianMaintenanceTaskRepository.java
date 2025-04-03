
package acme.features.technician.maintenanceTask;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.MaintenanceTask;
import acme.entities.maintenance.Task;

@Repository
public interface TechnicianMaintenanceTaskRepository extends AbstractRepository {

	@Query("select mt from MaintenanceTask mt")
	Collection<MaintenanceTask> findAllMaintenanceTask();

	@Query("select mt from MaintenanceTask mt where mt.id =:id")
	MaintenanceTask findMaintenanceTaskById(int id);

	@Query("select mr from MaintenanceRecord mr where mr.technician.id = :id")
	Collection<MaintenanceRecord> findMrByTechnicianId(int id);

	@Query("select t from Task t where t.technician.id = :id")
	Collection<Task> findTaskByTechnicianId(int id);

}
