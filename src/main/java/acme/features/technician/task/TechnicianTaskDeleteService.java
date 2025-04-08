
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.MaintenanceTask;
import acme.entities.maintenance.Task;
import acme.entities.maintenance.TaskType;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskDeleteService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository rp;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Task t;
		Technician technician;

		masterId = super.getRequest().getData("id", int.class);
		t = this.rp.findTaskById(masterId);
		technician = t == null ? null : t.getTechnician();
		status = technician != null && super.getRequest().getPrincipal().getActiveRealm().getId() == technician.getId() && t != null;

		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {
		Task t;
		int id;

		id = super.getRequest().getData("id", int.class);
		t = this.rp.findTaskById(id);

		super.getBuffer().addData(t);
	}
	@Override
	public void bind(final Task task) {

		super.bindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");

	}

	@Override
	public void validate(final Task task) {
		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(task.getDraftMode(), "draftMode", "technician.task.error.draftMode");
	}

	@Override
	public void perform(final Task task) {
		Collection<MaintenanceTask> mts;
		Collection<MaintenanceRecord> mrs;
		mts = this.rp.findMaintenanceTasks(task.getId());
		//		if (mts != null) {
		//			mrs = this.rp.findAllAsociatedMaintenaceRecords(task.getId());
		//
		//			this.rp.deleteAll(mrs);
		//		}
		//if (mts != null)
		System.out.println(mts);
		this.rp.deleteAll(mts);
		System.out.println(mts);
		//this.rp.delete(task);

	}

	@Override
	public void unbind(final Task task) {

		Dataset dataset;
		SelectChoices typeChoices;
		typeChoices = SelectChoices.from(TaskType.class, task.getType());
		dataset = super.unbindObject(task, "description", "priority", "estimatedDuration", "draftMode");
		dataset.put("technician", task.getTechnician().getUserAccount().getUsername());
		dataset.put("type", typeChoices);
		super.getResponse().addData(dataset);

	}

}
