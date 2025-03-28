
package acme.features.customer.booking;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S2.Booking;
import acme.entities.S2.ClassType;
import acme.realms.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Date moment;
		Customer customer;
		Booking booking;

		moment = MomentHelper.getCurrentMoment();
		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		booking = new Booking();
		booking.setPurchaseMoment(moment);
		booking.setLocatorCode("");  // Se generará luego
		booking.setTravelClass(ClassType.ECONOMY);  // Valor por defecto
		booking.setPrice(new Money());
		booking.setCustomer(customer);
		booking.setFlight(null);  // Se asignará al enviar el formulario

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "travelClass", "price", "flight");
	}

	@Override
	public void validate(final Booking booking) {
		boolean confirmation;
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");

		if (booking.getFlight() == null)
			super.state(false, "flight", "acme.validation.flight.required");

		if (booking.getLastCreditCardNibble() == null || booking.getLastCreditCardNibble().isBlank())
			super.state(false, "lastCreditCardNibble", "acme.validation.booking.credit-card-required");
	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		dataset = super.unbindObject(booking, "locatorCode", "travelClass", "price", "flight");

		super.getResponse().addData(dataset);
	}
}
