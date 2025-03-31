
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenance.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskShowService extends AbstractGuiService<Technician, Task> {

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
	public void unbind(final Task task) {

		Dataset dataset;
		//		SelectChoices mrChoices;
		SelectChoices aricraftChoices;
		Collection<Aircraft> aircrafts;
		aircrafts = this.rp.findAllAricraft();
		aricraftChoices = SelectChoices.from(aircrafts, "registrationNumber", task.getAircraft());
		//		Collection<MaintenanceRecord> mrs;
		//		mrs = this.rp.getAllMr();
		//		mrChoices = SelectChoices.from(mrs, "id", task.getMaintenanceRecord());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "maintenanceRecord", "draftMode");
		dataset.put("technician", task.getTechnician().getUserAccount().getUsername());
		//		dataset.put("maintenanceRecord", mrChoices.getSelected().getKey());
		//		dataset.put("mrs", mrs);
		dataset.put("aircraft", aricraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aricraftChoices);
		super.getResponse().addData(dataset);

	}
}
