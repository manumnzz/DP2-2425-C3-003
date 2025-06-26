
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;
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
import acme.realms.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Date moment = MomentHelper.getCurrentMoment();
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		Booking booking = new Booking();
		booking.setCustomer(customer);
		booking.setDraftMode(true);
		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "travelClass", "lastCreditCardNibble", "draftMode", "flight");
	}

	@Override
	public void validate(final Booking booking) {
		// Validación de locatorCode único
		if (!super.getBuffer().getErrors().hasErrors("locatorCode")) {
			Booking existing = this.repository.findByLocatorCodeAndIdNot(booking.getLocatorCode(), booking.getId());
			if (existing != null && existing.getId() != booking.getId())
				super.state(false, "locatorCode", "customer.booking.error.locatorCode.duplicate");
		}

		// Validación de draftMode
		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(booking.getDraftMode(), "draftMode", "customer.booking.error.draftMode");

		// Validación de travelClass obligatorio
		if (booking.getTravelClass() == null)
			super.state(false, "travelClass", "customer.booking.error.travelClass.required");

	}

	@Override
	public void perform(final Booking booking) {
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

		Dataset dataset = super.unbindObject(booking, "locatorCode", "travelClass", "lastCreditCardNibble", "draftMode", "flight");
		dataset.put("flights", flightChoices);

		// travelClass como SelectChoices

		SelectChoices travelClassChoices = SelectChoices.from(ClassType.class, booking.getTravelClass());
		dataset.put("travelClass", travelClassChoices);

		// price seguro
		Money price = new Money();
		if (booking.getFlight() != null && booking.getFlight().getCost() != null)
			price = booking.getFlight().getCost();
		else {
			price.setAmount(0.0);
			price.setCurrency("USD");
		}
		dataset.put("price", price);

		// lastCreditCardNibble como String o vacío
		dataset.put("lastCreditCardNibble", booking.getLastCreditCardNibble() != null ? booking.getLastCreditCardNibble() : "");

		// id y version
		dataset.put("id", booking.getId());
		dataset.put("version", booking.getVersion());

		// Añade el payload
		super.addPayload(dataset, booking, "customer.identifier");

		super.getResponse().addData(dataset);
	}

}
