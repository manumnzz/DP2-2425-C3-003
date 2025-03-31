
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskPublishService extends AbstractGuiService<Technician, Task> {

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
		status = super.getRequest().getPrincipal().hasRealm(technician) || t != null;

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

		super.bindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode", "aircraft");
		int mrId;
		MaintenanceRecord mr;
		mrId = super.getRequest().getData("maintenanceRecord", int.class);
		mr = this.rp.findMrById(mrId);
		task.setMaintenanceRecord(mr);

	}

	@Override
	public void validate(final Task task) {
		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(task.getDraftMode(), "draftMode", "technician.task.error.draftMode");
		if (!super.getBuffer().getErrors().hasErrors("aircraft"))
			super.state(task.getAircraft() == task.getMaintenanceRecord().getAircraft(), "aircraft", "technician.task.error.aricraft");

	}

	@Override
	public void perform(final Task task) {
		task.setDraftMode(false);
		this.rp.save(task);

	}

	@Override
	public void unbind(final Task task) {

		Dataset dataset;
		SelectChoices aricraftChoices;
		Collection<Aircraft> aircrafts;
		//		SelectChoices mrChoices;
		//		List<MaintenanceRecord> mrs;
		//		mrs = this.rp.getAllMr();
		aircrafts = this.rp.findAllAricraft();
		aricraftChoices = SelectChoices.from(aircrafts, "registrationNumber", task.getAircraft());
		//mrChoices = SelectChoices.from(mrs, "notes", task.getMaintenanceRecord());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "maintenanceRecord", "draftMode");
		dataset.put("technician", task.getTechnician().getUserAccount().getUsername());
		dataset.put("aircraft", aricraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aricraftChoices);
		//		dataset.put("maintenanceRecord", mrChoices.getSelected().getKey());
		//		dataset.put("mrs", mrs);

		super.getResponse().addData(dataset);
	}
}
