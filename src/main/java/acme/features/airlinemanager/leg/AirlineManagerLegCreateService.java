
package acme.features.airlinemanager.leg;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
import acme.client.helpers.StringHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Aircraft;
import acme.entities.AircraftStatus;
import acme.entities.Airport;
import acme.entities.S1.Flight;
import acme.entities.S1.FlightStatus;
import acme.entities.S1.Leg;
import acme.entities.S1.LegRepository;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegCreateService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repository;


	@Override
	public void authorise() {
		int flightId;
		Flight flight;
		AirlineManager manager;

		flightId = super.getRequest().getData("flightId", int.class);

		flight = this.repository.findFlightById(flightId);

		manager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();

		boolean status = flight != null && super.getRequest().getPrincipal().hasRealm(manager) && super.getRequest().getPrincipal().getAccountId() == flight.getAirlineManager().getUserAccount().getId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int masterId = super.getRequest().getData("flightId", int.class);
		Flight flight = this.repository.findFlightById(masterId);

		Leg leg = new Leg();
		leg.setFlight(flight);
		leg.setDraftMode(true);
		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {

		int aircraftId = super.getRequest().getData("aircraft", int.class);
		Aircraft aircraft = this.repository.findAircraftById(aircraftId);

		int arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
		Airport arrivalAirport = this.repository.findAirportById(arrivalAirportId);

		int departureAirportId = super.getRequest().getData("departureAirport", int.class);
		Airport departureAirport = this.repository.findAirportById(departureAirportId);

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");

		leg.setAircraft(aircraft);
		leg.setArrivalAirport(arrivalAirport);
		leg.setDepartureAirport(departureAirport);
	}

	@Override
	public void validate(final Leg leg) {
		if (leg == null) {
			super.state(false, "leg", "javax.validation.constraints.NotNull.message");
			return;
		}

		if (!StringHelper.isBlank(leg.getFlightNumber())) {
			String airlineIataCode = leg.getFlight().getAirlineManager().getAirline().getIataCode();
			boolean validFlightNumber = StringHelper.startsWith(leg.getFlightNumber(), airlineIataCode, true);
			super.state(validFlightNumber, "flightNumber", "acme.validation.leg.flightNumber.validFlightNumber.message");
		}

		if (leg.getScheduledDeparture() != null) {
			boolean isAfterCurrent = MomentHelper.isAfter(leg.getScheduledDeparture(), MomentHelper.getCurrentMoment());
			super.state(isAfterCurrent, "scheduledDeparture", "acme.validation.leg.scheduledArrival.must-be-after-current-moment.message");
		}

		if (leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null) {
			boolean validScheduledArrival = MomentHelper.isAfter(leg.getScheduledArrival(), leg.getScheduledDeparture());
			super.state(validScheduledArrival, "scheduledArrival", "acme.validation.leg.scheduledArrival.must-be-after.message");
		}

		if (!StringHelper.isBlank(leg.getFlightNumber())) {
			LegRepository repository = SpringHelper.getBean(LegRepository.class);
			boolean repeatedFlightNumber = repository.findByFlightNumber(leg.getFlightNumber(), leg.getId()).isEmpty();
			super.state(repeatedFlightNumber, "flightNumber", "acme.validation.leg.flightNumber.repeatedflightNumber.message");
		}

		if (leg.getAircraft() != null) {
			if (leg.getAircraft().getStatus().equals(AircraftStatus.UNDER_MAINTENANCE))
				super.state(false, "aircraft", "acme.validation.leg.aircraft.maintenance.message");

			if (leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null) {
				LegRepository repository = SpringHelper.getBean(LegRepository.class);
				List<Leg> otherLegs = repository.findByAircraftId(leg.getAircraft().getId(), leg.getId());
				for (Leg otherLeg : otherLegs)
					if (otherLeg.getScheduledArrival() != null && otherLeg.getScheduledDeparture() != null) {
						boolean isOverlapping = MomentHelper.isBefore(leg.getScheduledDeparture(), otherLeg.getScheduledArrival()) && MomentHelper.isAfter(leg.getScheduledArrival(), otherLeg.getScheduledDeparture());
						if (isOverlapping)
							super.state(false, "aircraft", "acme.validation.leg.aircraft.overlapping.message");
					}
			}
		}

		if (leg.getDepartureAirport() != null && leg.getArrivalAirport() != null) {
			boolean sameAirport = leg.getDepartureAirport().equals(leg.getArrivalAirport());
			super.state(!sameAirport, "arrivalAirport", "acme.validation.leg.aircraft.no-same-airport.message");
		}
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
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
