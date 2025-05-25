
package acme.features.administrator.aircraft;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Aircraft;

@GuiService
public class AdministratorAircraftShowService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AdministratorAircraftRepository repository;

	//AbstractGuiService interface ---------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Aircraft aircraft;
		Administrator administrator;
		int administratorId;

		masterId = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(masterId);
		administratorId = super.getRequest().getPrincipal().getActiveRealm().getId();
		administrator = this.repository.findAdministratorById(administratorId);

		status = super.getRequest().getPrincipal().hasRealm(administrator) || aircraft != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Aircraft aircraft;
		int id;

		id = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(id);

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset dataset;
		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "optionalDetails");
		dataset.put("airline", aircraft.getAirline().getName());
		super.getResponse().addData(dataset);
	}
}
