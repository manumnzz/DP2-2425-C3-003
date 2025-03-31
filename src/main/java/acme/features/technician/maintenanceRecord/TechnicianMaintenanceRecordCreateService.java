
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenance.MaintenanceRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository rp;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		MaintenanceRecord mr;
		Technician technician;
		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		mr = new MaintenanceRecord();
		mr.setTechnician(technician);
		mr.setDraftMode(true);

		super.getBuffer().addData(mr);
	}

	@Override
	public void bind(final MaintenanceRecord mr) {

		super.bindObject(mr, "maintenanceDate", "nextInspectionDue", "status", "estimatedCost", "notes", "aircraft", "draftMode");

	}

	@Override
	public void validate(final MaintenanceRecord mr) {
		if (!super.getBuffer().getErrors().hasErrors("nextInspectionDue"))
			super.state(mr.getNextInspectionDue().after(mr.getMaintenanceDate()), "nextInspectionDue", "technician.maintenanceRecord.error.nextInspectionDue");
	}

	@Override
	public void perform(final MaintenanceRecord mr) {
		this.rp.save(mr);

	}

	@Override
	public void unbind(final MaintenanceRecord mr) {
		assert mr != null;

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
