//
//package acme.features.authenticated.technician.dashboard;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.Query;
//
//import acme.client.repositories.AbstractRepository;
//import acme.entities.aircraft.Aircraft;
//import acme.entities.maintenance.MaintenanceRecord;
//import acme.realms.Technician;
//
//public interface TechnicianDashboardRepository extends AbstractRepository {
//
//	@Query("select t from Technician t where t.userAccount.id = :id")
//	Technician findTechnicianById(int id);
//
//	@Query("select count(mr) from MaintenanceRecord mr where mr.technician.id = :technicianId AND group by mr.status")
//	int numberOfMaintenaceRecordsByStatus(int technicianId);
//
//	@Query("select mr from MaintenanceRecords mr JOIN Task t on t.technician.id = :technicianId where mr.technician.id = :technicianId ORDER BY mr.nextInspectionDue")
//	MaintenanceRecord nearestMRinspection(int technicianId);
//
//	@Query("select a from Aircraft")
//	List<Aircraft> top5AircraftHigherTasks();
//
//	@Query("select min(mr.estimatedCost) FROM MaintenanceRecord where mr.technician.id = :technicianId")
//	int minimumEstimatedCost(int technicianId);
//
//	@Query("select max(mr.estimatedCost) FROM MaintenanceRecord where mr.technician.id = :technicianId")
//	int maximumEstimatedCost(int technicianId);
//
//	@Query("select avg(mr.estimatedCost) FROM MaintenanceRecord where mr.technician.id = :technicianId")
//	double averageEstimatedCost(int technicianId);
//
//	@Query("select stddev(mr.estimatedCost) FROM MaintenanceRecord where mr.technician.id = :technicianId")
//	double estandarDeviationEstimatedCost(int technicianId);
//
//	@Query("select min(t.estimatedDuration) FROM Task where t.technician.id = :technicianId")
//	int minimumEstimatedDuration(int technicianId);
//
//	@Query("select max(t.estimatedDuration) FROM Task where t.technician.id = :technicianId")
//	int maximumEstimatedDuration(int technicianId);
//
//	@Query("select avg(t.estimatedDuration) FROM Task where t.technician.id = :technicianId")
//	double averageEstimatedDuration(int technicianId);
//
//	@Query("select stddev(t.estimatedDuration) FROM Task where t.technician.id = :technicianId")
//	double estandarDeviationEstimatedDuration(int technicianId);
//
//}
