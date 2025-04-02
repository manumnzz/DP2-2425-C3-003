
package acme.features.technician.task;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.Task;
import acme.entities.maintenance.TaskType;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskCreateService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository rp;


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
		Task t;
		Technician technician;
		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		t = new Task();
		t.setTechnician(technician);
		t.setDraftMode(true);
		super.getBuffer().addData(t);
	}
	@Override
	public void bind(final Task task) {

		super.bindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");

	}

	@Override
	public void validate(final Task task) {

	}

	@Override
	public void perform(final Task t) {
		this.rp.save(t);

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
