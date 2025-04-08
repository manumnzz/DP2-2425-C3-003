
package acme.features.technician.maintenanceTask;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.MaintenanceTask;
import acme.entities.maintenance.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceTaskDeleteService extends AbstractGuiService<Technician, MaintenanceTask> {

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
		MaintenanceTask mt;
		mt = new MaintenanceTask();
		super.getBuffer().addData(mt);
	}

	@Override
	public void bind(final MaintenanceTask mt) {
		super.bindObject(mt, "maintenanceRecord", "task");

	}

	@Override
	public void validate(final MaintenanceTask mt) {
		//
	}

	@Override
	public void perform(final MaintenanceTask mt) {
		this.rp.delete(mt);
	}

	@Override
	public void unbind(final MaintenanceTask mt) {
		Dataset dataset;
		Technician technician;
		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		SelectChoices mrChoices;
		SelectChoices tChoices;
		Collection<MaintenanceRecord> mrs;
		Collection<Task> tasks;
		mrs = this.rp.findMrByTechnicianId(technician.getId());
		tasks = this.rp.findTaskByTechnicianId(technician.getId());
		mrChoices = SelectChoices.from(mrs, "id", mt.getMaintenanceRecord());
		tChoices = SelectChoices.from(tasks, "id", mt.getTask());
		dataset = super.unbindObject(mt, "maintenanceRecord", "task");
		//		dataset.put("maintenanceRecord", mrChoices.getSelected().getKey());
		//		dataset.put("mrs", mrs);
		//		dataset.put("task", tChoices.getSelected().getKey());
		//		dataset.put("tasks", tasks);
		super.getResponse().addData(dataset);

	}

}
