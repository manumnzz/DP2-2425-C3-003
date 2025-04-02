
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.MaintenanceRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordListService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository rp;


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
		Collection<MaintenanceRecord> objects;
		Principal principal;
		principal = super.getRequest().getPrincipal();
		objects = this.rp.findMrByTechnicianId(principal.getAccountId());

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;

		dataset = super.unbindObject(maintenanceRecord, "maintenanceDate", "status", "nextInspectionDue", "estimatedCost", "draftMode");
		dataset.put("status", maintenanceRecord.getStatus());
		dataset.put("aircraft", maintenanceRecord.getAircraft().getRegistrationNumber());
		dataset.put("technician", maintenanceRecord.getTechnician().getUserAccount().getUsername());
		super.getResponse().addData(dataset);

	}

}
