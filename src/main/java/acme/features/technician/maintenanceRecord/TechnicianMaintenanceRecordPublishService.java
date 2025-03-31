
package acme.features.technician.maintenanceRecord;

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
public class TechnicianMaintenanceRecordPublishService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository rp;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		MaintenanceRecord mr;
		Technician technician;

		masterId = super.getRequest().getData("id", int.class);
		mr = this.rp.findMrById(masterId);
		technician = mr == null ? null : mr.getTechnician();
		status = super.getRequest().getPrincipal().hasRealm(technician) || mr != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord mr;
		int id;

		id = super.getRequest().getData("id", int.class);
		mr = this.rp.findMrById(id);

		super.getBuffer().addData(mr);
	}

	@Override
	public void bind(final MaintenanceRecord mr) {

		super.bindObject(mr, "maintenanceDate", "nextInspectionDue", "status", "estimatedCost", "notes", "aircraft", "draftMode");

	}

	@Override
	public void validate(final MaintenanceRecord mr) {
		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(mr.getDraftMode() == true, "draftMode", "technician.maintenanceRecord.error.draftMode");

		if (!super.getBuffer().getErrors().hasErrors("nextInspectionDue"))
			super.state(mr.getNextInspectionDue().after(mr.getMaintenanceDate()), "nextInspectionDue", "technician.maintenanceRecord.error.nextInspectionDue");

		Collection<Task> tasks;
		int totalTasks;
		boolean allDraftMode;
		tasks = this.rp.getAllAsociatedPublishedTasks(mr);
		totalTasks = tasks.size();
		allDraftMode = tasks.stream().allMatch(Task::getDraftMode);
		System.out.println(totalTasks);
		System.out.println(allDraftMode);
		super.state(totalTasks >= 1, "*", "technician.maintenaceRecord.form.error.not-enough-tasks");
		super.state(!allDraftMode, "*", "technician.maintenaceRecord.form.error.not-all-published");

	}

	@Override
	public void perform(final MaintenanceRecord mr) {
		mr.setDraftMode(false);
		this.rp.save(mr);

	}

	@Override
	public void unbind(final MaintenanceRecord mr) {

		Dataset dataset;
		SelectChoices aricraftChoices;
		Collection<Aircraft> aircrafts;

		aircrafts = this.rp.findAllAricraft();
		aricraftChoices = SelectChoices.from(aircrafts, "registrationNumber", mr.getAircraft());

		dataset = super.unbindObject(mr, "maintenanceDate", "nextInspectionDue", "status", "estimatedCost", "notes", "draftMode");
		dataset.put("aircraft", aricraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aricraftChoices);

		super.getResponse().addData(dataset);

	}

}
