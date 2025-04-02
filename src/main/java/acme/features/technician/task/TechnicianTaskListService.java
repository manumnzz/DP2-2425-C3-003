
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskListService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository rp;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Task> objects;
		Principal principal;
		principal = super.getRequest().getPrincipal();
		objects = this.rp.findTaskByTechnicianId(principal.getAccountId());

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Task task) {

		Dataset dataset;

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");
		dataset.put("technician", task.getTechnician().getUserAccount().getUsername());
		super.getResponse().addData(dataset);

	}

}
