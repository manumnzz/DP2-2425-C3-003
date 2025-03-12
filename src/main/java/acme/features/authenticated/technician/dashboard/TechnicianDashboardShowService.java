//
//package acme.features.authenticated.technician.dashboard;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import acme.client.services.AbstractService;
//import acme.entities.maintenance.MaintenanceRecord;
//import acme.forms.TechnicianDashboard;
//import acme.realms.Technician;
//
//@Service
//public class TechnicianDashboardShowService extends AbstractService<Technician, TechnicianDashboard> {
//
//	@Autowired
//	private TechnicianDashboardRepository repository;
//
//
//	@Override
//	public void authorise() {
//		boolean status;
//		int id;
//
//		id = super.getRequest().getPrincipal().getAccountId();
//		Technician technician = this.repository.findTechnicianById(id);
//		status = technician != null && super.getRequest().getPrincipal().hasRealmOfType(Technician.class);
//
//		super.getResponse().setAuthorised(status);
//	}
//	@Override
//	public void load() {
//		int technicianId;
//		technicianId = super.getRequest().getPrincipal().getAccountId();
//
//		TechnicianDashboard dashboard;
//		int numberOfMaintenaceRecordsByStatus;
//		MaintenanceRecord nearestMRinspection;
//		//List<Aircraft> top5AircraftHigherTasks;
//		int minimumEstimatedCost;
//		int maximumEstimatedCost;
//		double averageEstimatedCost;
//		double estandarDeviationEstimatedCost;
//		int minimumEstimatedDuration;
//		int maximumEstimatedDuration;
//		double averageEstimatedDuration;
//		double estandarDeviationEstimatedDuration;
//
//		numberOfMaintenaceRecordsByStatus = this.repository.numberOfMaintenaceRecordsByStatus(technicianId);
//		nearestMRinspection = this.repository.nearestMRinspection(technicianId);
//		minimumEstimatedCost = this.repository.minimumEstimatedCost(technicianId);
//		maximumEstimatedCost = this.repository.maximumEstimatedCost(technicianId);
//		averageEstimatedCost = this.repository.averageEstimatedCost(technicianId);
//		estandarDeviationEstimatedCost = this.repository.estandarDeviationEstimatedCost(technicianId);
//		minimumEstimatedDuration = this.repository.minimumEstimatedDuration(technicianId);
//		maximumEstimatedDuration = this.repository.maximumEstimatedDuration(technicianId);
//		averageEstimatedDuration = this.repository.averageEstimatedDuration(technicianId);
//		estandarDeviationEstimatedDuration = this.repository.estandarDeviationEstimatedDuration(technicianId);
//
//		dashboard = new TechnicianDashboard();
//		MaintenanceRecord mr;
//		mr = new MaintenanceRecord();
//		dashboard.setNumberOfMaintenaceRecordsByStatus(0);
//		dashboard.setNearestMRinspection(mr);
//		dashboard.setMinimumEstimatedCost(0);
//		dashboard.setMaximumEstimatedCost(0);
//		dashboard.setAverageEstimatedCost(0);
//		dashboard.setEstandarDeviationEstimatedCost(0);
//		dashboard.setMinimumEstimatedDuration(0);
//		dashboard.setMaximumEstimatedDuration(0);
//		dashboard.setAverageEstimatedDuration(0);
//		dashboard.setEstandarDeviationEstimatedDuration(0);
//
//	}
//}
