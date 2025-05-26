
package acme.features.technician.maintenanceTask;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.MaintenanceTask;
import acme.entities.maintenance.Task;
import acme.realms.Technician;

@Repository
public interface TechnicianMaintenanceTaskRepository extends AbstractRepository {

	@Query("select mt from MaintenanceTask mt where mt.maintenanceRecord.technician.id = :id")
	Collection<MaintenanceTask> findAllMaintenanceTaskById(int id);

	@Query("select mt from MaintenanceTask mt where mt.id =:id")
	MaintenanceTask findMaintenanceTaskById(int id);

	@Query("select mr from MaintenanceRecord mr where mr.technician.id = :id")
	Collection<MaintenanceRecord> findMrByTechnicianId(int id);

	@Query("select t from Task t where t.technician.id = :id")
	Collection<Task> findTaskByTechnicianId(int id);

	@Query("select t from Task t where t.id = :id")
	Task findTaskById(int id);

	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select t from Technician t where t.id = :id")
	Technician findOneTechnicianById(int id);

	@Query("select mt from MaintenanceTask mt where mt.maintenanceRecord.id = :id and mt.task.id = :id2")
	MaintenanceTask findOneMtByMrandTask(int id, int id2);

}
