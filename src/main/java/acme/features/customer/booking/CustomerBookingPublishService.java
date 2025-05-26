
package acme.features.customer.booking;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S1.Flight;
import acme.entities.S1.Leg;
import acme.entities.S2.Booking;
import acme.entities.S2.ClassType;
import acme.features.customer.bookingPassenger.CustomerBookingPassengerRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository			repository;

	@Autowired
	private CustomerBookingPassengerRepository	repositoryBP;


	@Override
	public void authorise() {
		int bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.findBookingById(bookingId);

		boolean status = booking != null && super.getRequest().getPrincipal().hasRealm(booking.getCustomer()) && booking.isDraftMode(); // Solo permite publicar si está en borrador

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.findBookingById(bookingId);
		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		// No hay campos editables en publish, así que normalmente se deja vacío
	}

	@Override
	public void validate(final Booking booking) {
		// lastCreditCardNibble obligatorio
		if (!super.getBuffer().getErrors().hasErrors("lastCreditCardNibble"))
			if (booking.getLastCreditCardNibble() == null || booking.getLastCreditCardNibble().trim().isEmpty())
				super.state(false, "lastCreditCardNibble", "customer.booking.error.lastCreditCardNibble.required.");

		// Pasajeros publicados obligatorios
		int bookingId = booking.getId();
		int publishedPassengers = this.repository.countPublishedByBookingId(bookingId);
		int draftPassengers = this.repository.countDraftByBookingId(bookingId);

		if (publishedPassengers == 0)
			super.state(false, "passengers", "customer.booking.error.passengers.required.");
		if (draftPassengers > 0)
			super.state(false, "passengers", "customer.booking.error.passengers.draftNotAllowed.");
	}

	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(false);
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		// Combo de vuelos
		Collection<Flight> flights = this.repository.findAllFlights();
		SelectChoices flightChoices = new SelectChoices();

		for (Flight flight : flights) {
			List<Leg> legs = this.repository.findByFlightIdOrdered(flight.getId());
			if (!legs.isEmpty()) {
				Leg firstLeg = legs.get(0);
				Leg lastLeg = legs.get(legs.size() - 1);

				String origin = firstLeg.getDepartureAirport().getIataCode();
				String destination = lastLeg.getArrivalAirport().getIataCode();
				String departure = firstLeg.getScheduledDeparture().toString();
				String arrival = lastLeg.getScheduledArrival().toString();

				String label = origin + " – " + destination + " (" + departure + " – " + arrival + ")";
				boolean selected = booking.getFlight() != null && booking.getFlight().getId() == flight.getId();
				flightChoices.add(String.valueOf(flight.getId()), label, selected);
			}
		}

		Dataset dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCreditCardNibble", "draftMode", "flight");
		dataset.put("flights", flightChoices);

		// travelClass como SelectChoices
		SelectChoices travelClassChoices = SelectChoices.from(ClassType.class, booking.getTravelClass());
		dataset.put("travelClass", travelClassChoices);

		// Cálculo seguro del precio
		Money costPerPassenger = new Money();
		if (booking.getFlight() != null && booking.getFlight().getCost() != null)
			costPerPassenger = booking.getFlight().getCost();
		else {
			costPerPassenger.setAmount(0.0);
			costPerPassenger.setCurrency("USD");
		}
		int passengerCount = 0;
		try {
			passengerCount = this.repositoryBP.findPublishedByBookingId(booking.getId()).size();
		} catch (Exception e) {
			passengerCount = 0;
		}
		Money totalPrice = new Money();
		totalPrice.setAmount(costPerPassenger.getAmount() * passengerCount);
		totalPrice.setCurrency(costPerPassenger.getCurrency());
		dataset.put("price", totalPrice);

		// lastCreditCardNibble como String o vacío
		dataset.put("lastCreditCardNibble", booking.getLastCreditCardNibble() != null ? booking.getLastCreditCardNibble() : "");

		// purchaseMoment como String o vacío
		dataset.put("purchaseMoment", booking.getPurchaseMoment() != null ? booking.getPurchaseMoment().toString() : "");

		// id y version
		dataset.put("id", booking.getId());
		dataset.put("version", booking.getVersion());

		super.getResponse().addData(dataset);
	}
}
