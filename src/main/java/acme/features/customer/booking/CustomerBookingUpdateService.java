
package acme.features.customer.booking;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S1.Flight;
import acme.entities.S1.Leg;
import acme.entities.S2.Booking;
import acme.entities.S2.ClassType;
import acme.realms.Customer;

@GuiService
public class CustomerBookingUpdateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.findBookingById(bookingId);

		boolean status = booking != null && super.getRequest().getPrincipal().hasRealm(booking.getCustomer()) && booking.getDraftMode();

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
		super.bindObject(booking, "locatorCode", "travelClass", "lastCreditCardNibble", "draftMode", "flight");
	}

	@Override
	public void validate(final Booking booking) {
		if (!super.getBuffer().getErrors().hasErrors("locatorCode")) {
			Booking existing = this.repository.findByLocatorCodeAndIdNot(booking.getLocatorCode(), booking.getId());
			if (existing != null)
				super.state(false, "locatorCode", "customer.booking.error.locatorCode.duplicate");
		}
		//if (booking.getLastCreditCardNibble() != null)
		//super.state(false, "lastCreditCardNibble", "acme.validation.booking.no-modify-after-payment");
		//if (!super.getBuffer().getErrors().hasErrors("draftMode"))
		//	super.state(booking.isDraftMode(), "draftMode", "customer.booking.error.draftMode");
	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
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

		Dataset dataset = super.unbindObject(booking, "locatorCode", "travelClass", "lastCreditCardNibble", "draftMode", "flight");
		dataset.put("flights", flightChoices);

		dataset.put("price", booking.getPrice());

		// travelClass como SelectChoices
		SelectChoices travelClassChoices = SelectChoices.from(ClassType.class, booking.getTravelClass());
		dataset.put("travelClass", travelClassChoices);

		// lastCreditCardNibble como String o vacío
		dataset.put("lastCreditCardNibble", booking.getLastCreditCardNibble() != null ? booking.getLastCreditCardNibble() : "");

		// purchaseMoment como String o vacío (si tu test lo espera)
		dataset.put("purchaseMoment", booking.getPurchaseMoment() != null ? booking.getPurchaseMoment().toString() : "");

		// id y version
		dataset.put("id", booking.getId());
		dataset.put("version", booking.getVersion());

		super.getResponse().addData(dataset);
	}

}
