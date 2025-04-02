
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
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordShowService extends AbstractGuiService<Technician, MaintenanceRecord> {

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
