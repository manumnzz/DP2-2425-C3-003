
package acme.features.airlinemanager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S1.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegListService extends AbstractGuiService<AirlineManager, Leg> {

	//Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerLegRepository repository;

	//AbstractGuiService interface ---------------------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Leg> legs;
		int id;

		id = super.getRequest().getPrincipal().getActiveRealm().getId();
		legs = this.repository.findLegsByAirlineManagerId(id);

		super.getBuffer().addData(legs);

	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "departureAirport.name", "arrivalAirport.name", "draftMode");
		super.addPayload(dataset, leg, "aircraft", "duration", "status", "flightNumber");

		super.getResponse().addData(dataset);
	}

}
