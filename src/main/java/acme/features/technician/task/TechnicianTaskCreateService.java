
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

		super.bindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode", "aircraft");
		int mrId;
		MaintenanceRecord mr;
		mrId = super.getRequest().getData("maintenanceRecord", int.class);
		mr = this.rp.findMrById(mrId);
		task.setMaintenanceRecord(mr);

	}

	@Override
	public void validate(final Task task) {
		if (!super.getBuffer().getErrors().hasErrors("aircraft"))
			super.state(task.getAircraft() == task.getMaintenanceRecord().getAircraft(), "aircraft", "technician.task.error.aricraft");

	}

	@Override
	public void perform(final Task t) {
		this.rp.save(t);

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
