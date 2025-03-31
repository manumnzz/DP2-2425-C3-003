
package acme.features.administrator.aircraft;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Aircraft;
import acme.entities.AircraftStatus;
import acme.entities.Airline;

@GuiService
public class AdministratorAircraftDisableService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AdministratorAircraftRepository repository;

	//AbstractGuiService interface ---------------------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
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
	public void bind(final Aircraft aircraft) {
		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "optionalDetails");
		String airlineName;
		Airline a;
		airlineName = super.getRequest().getData("airline", String.class);
		a = this.repository.findAirlineByName(airlineName);
		aircraft.setAirline(a);
	}

	@Override
	public void validate(final Aircraft aircraft) {
		if (!super.getBuffer().getErrors().hasErrors("status"))
			super.state(aircraft.getStatus().equals(AircraftStatus.ACTIVE), "status", "technician.task.error.status");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		aircraft.setStatus(AircraftStatus.UNDER_MAINTENANCE);
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset dataset;
		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "optionalDetails");
		dataset.put("airline", aircraft.getAirline().getName());
		super.getResponse().addData(dataset);
	}

}
