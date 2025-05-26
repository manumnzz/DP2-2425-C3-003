
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
public class TechnicianMaintenanceTaskCreateService extends AbstractGuiService<Technician, MaintenanceTask> {

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
		int maintenanceRecordId = super.getRequest().getData("maintenanceRecord", int.class);
		int taskId = super.getRequest().getData("task", int.class);

		MaintenanceRecord maintenanceRecord = this.rp.findMaintenanceRecordById(maintenanceRecordId);
		Task task = this.rp.findTaskById(taskId);

		mt.setMaintenanceRecord(maintenanceRecord);
		mt.setTask(task);
	}

	@Override
	public void validate(final MaintenanceTask mt) {
		/*
		 * No deben existir relaciones repetidas, es decir, mismo proyecto e historia de usuario
		 * No se pueden crear asignaciones a proyectos ya publicados
		 * No se pueden crear asignaciones a proyectos de otros managers
		 * No se pueden crear asignaciones con historias de usuario no publicadas de otros managers
		 */
		MaintenanceRecord mr;
		Task task;
		Technician technician;

		mr = mt.getMaintenanceRecord();
		task = mt.getTask();
		technician = this.rp.findOneTechnicianById(super.getRequest().getPrincipal().getActiveRealm().getId());

		if (!super.getBuffer().getErrors().hasErrors("maintenanceRecord")) {
			MaintenanceTask existing;

			if (task != null) {
				existing = this.rp.findOneMtByMrandTask(mr.getId(), task.getId());
				super.state(existing == null, "*", "technician.maintenance-task.form.error-existing");
			}

			super.state(mr.getDraftMode(), "maintenanceRecord", "technician.maintenance-task.form.error-draftMode");

			super.state(mr.getTechnician().equals(technician), "*", "technician.maintenance-task.form.error-technician-mr");
		}

		if (!super.getBuffer().getErrors().hasErrors("task") && task != null)
			super.state(task.getTechnician().equals(technician) || !task.getDraftMode(), "task", "technician.maintenance-task.form.error-technician-task");
	}

	@Override
	public void perform(final MaintenanceTask mt) {
		this.rp.save(mt);

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
		dataset.put("maintenanceRecord", mrChoices.getSelected().getKey());
		dataset.put("mrs", mrChoices);
		dataset.put("task", tChoices.getSelected().getKey());
		dataset.put("tasks", tChoices);
		super.getResponse().addData(dataset);
	}

}
