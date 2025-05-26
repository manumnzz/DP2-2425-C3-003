
package acme.features.technician.maintenanceTask;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.MaintenanceTask;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceTaskListService extends AbstractGuiService<Technician, MaintenanceTask> {

	@Autowired
	private TechnicianMaintenanceTaskRepository rp;


	@Override
	public void authorise() {
		boolean status;
		Technician technician;
		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		status = technician != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<MaintenanceTask> objects;
		Technician technician;
		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		objects = this.rp.findAllMaintenanceTaskById(technician.getId());
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final MaintenanceTask maintenanceTask) {
		Dataset dataset;
		dataset = super.unbindObject(maintenanceTask, "maintenanceRecord", "task");
		super.getResponse().addData(dataset);

	}
}
