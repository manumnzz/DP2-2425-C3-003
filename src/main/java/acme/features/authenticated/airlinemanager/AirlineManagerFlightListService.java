
package acme.features.authenticated.airlinemanager;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S1.Flight;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightListService extends AbstractGuiService<Authenticated, AirlineManager> {

	//Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightRepository repository;

	//AbstractGuiService interface ---------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int identifier;
		Flight flight;

		identifier = super.getRequest().getPrincipal().getAccountId();
		flight = this.repository.findFlightByAirlineManagerId(identifier);
		status = flight != null && super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight airlineManager;
		int id;

		id = super.getRequest().getPrincipal().getAccountId();
		airlineManager = this.repository.findFlightByAirlineManagerId(id);

		super.getBuffer().addData(airlineManager);
	}
}
