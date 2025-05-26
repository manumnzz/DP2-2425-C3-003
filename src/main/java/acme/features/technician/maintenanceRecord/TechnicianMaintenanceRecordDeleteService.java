
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.MaintenanceStatus;
import acme.entities.maintenance.MaintenanceTask;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordDeleteService extends AbstractGuiService<Technician, MaintenanceRecord> {

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
		status = technician != null && super.getRequest().getPrincipal().getActiveRealm().getId() == technician.getId() && mr != null;

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
			super.state(mr.getDraftMode(), "draftMode", "technician.maintenanceRecord.error.draftMode");

	}

	@Override
	public void perform(final MaintenanceRecord mr) {
		Collection<MaintenanceTask> mts;
		mts = this.rp.findMaintenanceTaskByMrId(mr.getId());
		this.rp.deleteAll(mts);
		this.rp.delete(mr);
	}

	@Override
	public void unbind(final MaintenanceRecord mr) {

		Dataset dataset;
		SelectChoices aricraftChoices;
		Collection<Aircraft> aircrafts;
		SelectChoices statusChoices;
		statusChoices = SelectChoices.from(MaintenanceStatus.class, mr.getStatus());
		aircrafts = this.rp.findAllAricraft();
		aricraftChoices = SelectChoices.from(aircrafts, "registrationNumber", mr.getAircraft());
		dataset = super.unbindObject(mr, "maintenanceDate", "nextInspectionDue", "estimatedCost", "notes", "draftMode");
		dataset.put("status", statusChoices);
		dataset.put("aircraft", aricraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aricraftChoices);
		super.getResponse().addData(dataset);

	}

}
