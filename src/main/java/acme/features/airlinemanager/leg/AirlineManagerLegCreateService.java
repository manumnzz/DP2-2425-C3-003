
package acme.features.airlinemanager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.StringHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Aircraft;
import acme.entities.AircraftStatus;
import acme.entities.Airport;
import acme.entities.S1.Flight;
import acme.entities.S1.FlightStatus;
import acme.entities.S1.Leg;
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

		if (status && super.getRequest().getMethod().equals("POST")) {

			int id = super.getRequest().getData("id", int.class);

			if (id != 0)
				status = false;
			else {

				Integer aircraftId = super.getRequest().getData("aircraft", Integer.class);
				Aircraft aircraft = null;

				if (aircraftId != null && aircraftId != 0)
					aircraft = this.repository.findAircraftById(aircraftId);

				Integer arrivalAirportId = super.getRequest().getData("arrivalAirport", Integer.class);
				Airport arrivalAirport = null;
				if (arrivalAirportId != null && arrivalAirportId != 0)
					arrivalAirport = this.repository.findAirportById(arrivalAirportId);

				Integer departureAirportId = super.getRequest().getData("departureAirport", Integer.class);
				Airport departureAirport = null;
				if (departureAirportId != null && departureAirportId != 0)
					departureAirport = this.repository.findAirportById(departureAirportId);

				boolean aircraftAutorization = aircraftId != null && aircraftId != 0 && aircraft == null;
				boolean departureAirportAutorization = departureAirportId != null && departureAirportId != 0 && departureAirport == null;
				boolean arrivalAirportAutorization = arrivalAirportId != null && arrivalAirportId != 0 && arrivalAirport == null;
				if (aircraftAutorization || departureAirportAutorization || arrivalAirportAutorization)
					status = false;
			}
		}

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

		// FlightNumber empieza por el IATA de la aerolínea
		if (!StringHelper.isBlank(leg.getFlightNumber()) && leg.getFlight() != null) {
			String airlineIataCode = leg.getFlight().getAirlineManager().getAirline().getIataCode();
			boolean validFlightNumber = StringHelper.startsWith(leg.getFlightNumber(), airlineIataCode, true);
			super.state(validFlightNumber, "flightNumber", "acme.validation.leg.flightNumber.validFlightNumber.message");
		}

		// ScheduledDeparture después del momento actual
		if (leg.getScheduledDeparture() != null) {
			boolean isAfterCurrent = MomentHelper.isAfter(leg.getScheduledDeparture(), MomentHelper.getCurrentMoment());
			super.state(isAfterCurrent, "scheduledDeparture", "acme.validation.leg.scheduledArrival.must-be-after-current-moment.message");
		}

		// ScheduledArrival después de ScheduledDeparture
		if (leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null) {
			boolean validScheduledArrival = MomentHelper.isAfter(leg.getScheduledArrival(), leg.getScheduledDeparture());
			super.state(validScheduledArrival, "scheduledArrival", "acme.validation.leg.scheduledArrival.must-be-after.message");
		}

		// FlightNumber único
		if (!StringHelper.isBlank(leg.getFlightNumber())) {
			Collection<Leg> matches = this.repository.findByFlightNumber(leg.getFlightNumber(), leg.getId());
			boolean repeatedFlightNumber = matches.isEmpty();
			super.state(repeatedFlightNumber, "flightNumber", "acme.validation.leg.flightNumber.repeatedflightNumber.message");
		}

		// Avión no en mantenimiento
		if (leg.getAircraft() != null && leg.getAircraft().getStatus() == AircraftStatus.UNDER_MAINTENANCE)
			super.state(false, "aircraft", "acme.validation.leg.aircraft.maintenance.message");

		// Conflictos temporales con otros legs del mismo vuelo
		if (leg.getFlight() != null && leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null) {
			Collection<Leg> otherLegsSameFlight = this.repository.findByFlightId(leg.getFlight().getId(), leg.getId());
			for (Leg otherLeg : otherLegsSameFlight)
				if (otherLeg.getScheduledArrival() != null && otherLeg.getScheduledDeparture() != null) {
					boolean isOverlapping = !(MomentHelper.isBefore(leg.getScheduledArrival(), otherLeg.getScheduledDeparture()) || MomentHelper.isAfter(leg.getScheduledDeparture(), otherLeg.getScheduledArrival()));
					if (isOverlapping) {
						super.state(false, "scheduledArrival", "acme.validation.leg.flight.overlapping.message");
						break;
					}
				}
		}

		// Conflictos del avión con otros vuelos
		if (leg.getAircraft() != null && leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null) {
			Collection<Leg> otherLegs = this.repository.findByAircraftId(leg.getAircraft().getId(), leg.getId());
			for (Leg otherLeg : otherLegs)
				if (otherLeg.getScheduledArrival() != null && otherLeg.getScheduledDeparture() != null) {
					boolean isOverlapping = MomentHelper.isBefore(leg.getScheduledDeparture(), otherLeg.getScheduledArrival()) && MomentHelper.isAfter(leg.getScheduledArrival(), otherLeg.getScheduledDeparture());
					if (isOverlapping) {
						super.state(false, "aircraft", "acme.validation.leg.aircraft.overlapping.message");
						break;
					}
				}
		}

		// Aeropuerto de salida y llegada no puede ser el mismo
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
