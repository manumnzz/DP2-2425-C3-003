
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.Task;
import acme.realms.Technician;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("select t from Technician t where t.userAccount.id = :id")
	Technician findTechnicianById(int id);

	@Query("select mr from MaintenanceRecord mr where mr.technician.userAccount.id = :id")
	Collection<MaintenanceRecord> findMrByTechnicianId(int id);

	@Query("select mr from MaintenanceRecord mr")
	Collection<MaintenanceRecord> findAllMr(int id);

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAricraft();

	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findMrById(int id);

	@Query("select  mt.task from MaintenaceTask mt where mt.maintenanceRecord = :mr")
	Collection<Task> getAllAsociatedTasks(MaintenanceRecord mr);

	@Query("select mt.task from MaintenaceTask mt where mt.maintenanceRecord = :mr AND mt.task.draftMode = false")
	Collection<Task> getAllAsociatedPublishedTasks(MaintenanceRecord mr);

}
