
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskListAsociatedService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository rp;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		MaintenanceRecord mr;
		masterId = super.getRequest().getData("masterId", int.class);
		mr = this.rp.findMrById(masterId);
		status = mr != null && super.getRequest().getPrincipal().hasRealm(mr.getTechnician());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Task> objects;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		objects = this.rp.findAllAsociatedTasks(masterId);
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;
		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");
		dataset.put("technician", task.getTechnician().getUserAccount().getUsername());
		super.getResponse().addData(dataset);

	}

	@Override
	public void unbind(final Collection<Task> tasks) {
		int masterId;
		MaintenanceRecord mr;
		final boolean showCreate;

		masterId = super.getRequest().getData("masterId", int.class);
		mr = this.rp.findMrById(masterId);
		showCreate = mr.getDraftMode() && super.getRequest().getPrincipal().hasRealm(mr.getTechnician());
		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}

}
