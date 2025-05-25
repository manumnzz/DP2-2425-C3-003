
package acme.features.airlinemanager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Aircraft;
import acme.entities.Airport;
import acme.entities.S1.FlightStatus;
import acme.entities.S1.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegShowService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repository;


	@Override
	public void authorise() {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("id", int.class);

		leg = this.repository.findLegById(legId);

		boolean status = leg != null && super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class) && super.getRequest().getPrincipal().getAccountId() == leg.getFlight().getAirlineManager().getUserAccount().getId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		Collection<Airport> airports = this.repository.findAllAirports();
		Collection<Aircraft> aircrafts = this.repository.findAllAircrafts();

		dataset = super.unbindObject(leg, "flightNumber", "scheduledArrival", "scheduledDeparture", "status", "draftMode");
		SelectChoices choiceAircrafts = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());
		SelectChoices choiceDepartureAirports = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		SelectChoices choiceArrivalAirports = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());
		SelectChoices choiceStatuses = SelectChoices.from(FlightStatus.class, leg.getStatus());

		dataset.put("airlineIata", leg.getFlight().getAirlineManager().getAirline().getIataCode());
		dataset.put("aircraft", choiceAircrafts.getSelected().getKey());
		dataset.put("aircrafts", choiceAircrafts);
		dataset.put("departureAirport", choiceDepartureAirports.getSelected().getKey());
		dataset.put("departureAirports", choiceDepartureAirports);
		dataset.put("arrivalAirport", choiceArrivalAirports.getSelected().getKey());
		dataset.put("arrivalAirports", choiceArrivalAirports);
		dataset.put("statuses", choiceStatuses);
		dataset.put("duration", leg.getDuration());

		dataset.put("flightId", leg.getFlight().getId());

		super.getResponse().addData(dataset);
	}
}
