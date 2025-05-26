
package acme.features.technician.task;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Aircraft;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.MaintenanceTask;
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

	@Query("select  mt.task from MaintenanceTask mt where mt.maintenanceRecord.id = :mrId")
	Task findTaskByMrId(int mrId);

	@Query("select  mt.maintenanceRecord from MaintenanceTask mt where mt.maintenanceRecord.id = :mrId")
	MaintenanceRecord findMrMtById(int mrId);

	@Query("select  mr from MaintenanceRecord mr where mr.id = :mrId")
	MaintenanceRecord findMrById(int mrId);

	@Query("select mr from MaintenanceRecord mr where mr.draftMode = false")
	List<MaintenanceRecord> getAllMr();

	@Query("select  mt.task from MaintenanceTask mt where mt.maintenanceRecord.id = :mrId")
	Collection<Task> findAllAsociatedTasks(int mrId);

	@Query("select  mt.maintenanceRecord from MaintenanceTask mt where mt.task.id = :id")
	Set<MaintenanceRecord> findAllAsociatedMaintenaceRecords(int id);

	@Query("select mt from MaintenanceTask mt where mt.task.id = :id")
	Collection<MaintenanceTask> findMaintenanceTasks(int id);

}
