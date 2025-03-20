
package acme.features.authenticated.airlinemanager;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.AirlineManager;

@GuiController
public class AirlineManagerFlightController extends AbstractGuiController<Authenticated, AirlineManager> {

	//Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightListService listService;

	//Constructors ---------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}
}
