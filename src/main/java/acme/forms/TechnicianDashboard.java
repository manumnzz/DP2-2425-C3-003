
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.entities.Aircraft;
import acme.entities.maintenance.MaintenanceRecord;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;
	// Attributes -------------------------------------------------------------

	int							numberOfMaintenaceRecordsByStatus;
	MaintenanceRecord			nearestMRinspection;

	List<Aircraft>				top5AircraftHigherTasks;

	int							minimumEstimatedCost;
	int							maximumEstimatedCost;
	double						averageEstimatedCost;
	double						estandarDeviationEstimatedCost;

	int							minimumEstimatedDuration;
	int							maximumEstimatedDuration;
	double						averageEstimatedDuration;
	double						estandarDeviationEstimatedDuration;
	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
