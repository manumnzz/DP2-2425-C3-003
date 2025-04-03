
package acme.features.administrator.airport;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Airport;
import acme.entities.OperationalScope;

@GuiService
public class AdministratorAirportShowService extends AbstractGuiService<Administrator, Airport> {

	//Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirportRepository repository;

	//AbstractGuiService interface ---------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Airport airport;
		Administrator administrator;
		int administratorId;

		masterId = super.getRequest().getData("id", int.class);
		airport = this.repository.findAirportById(masterId);
		administratorId = super.getRequest().getPrincipal().getActiveRealm().getId();
		administrator = this.repository.findAdministratorById(administratorId);

		status = super.getRequest().getPrincipal().hasRealm(administrator) || airport != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Airport airport;
		int id;

		id = super.getRequest().getData("id", int.class);
		airport = this.repository.findAirportById(id);

		super.getBuffer().addData(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;
		SelectChoices choices;

		choices = SelectChoices.from(OperationalScope.class, airport.getOperationalScope());

		dataset = super.unbindObject(airport, "name", "iataCode", "city", "country", "website", "email", "phone");
		dataset.put("operationalScopes", choices);

		super.getResponse().addData(dataset);
	}
}
