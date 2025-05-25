
package acme.features.customer.booking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
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

		boolean status = booking != null && super.getRequest().getPrincipal().hasRealm(booking.getCustomer());

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
		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCreditCardNibble", "draftMode", "flight");
	}

	@Override
	public void validate(final Booking booking) {
		if (!super.getBuffer().getErrors().hasErrors("locatorCode")) {
			Booking existing = this.repository.findBookingByLocatorCode(booking.getLocatorCode());
			if (existing != null && existing.getId() != booking.getId())
				super.state(false, "locatorCode", "customer.booking.error.locatorCode.duplicate");
		}
		//if (booking.getLastCreditCardNibble() != null)
		//super.state(false, "lastCreditCardNibble", "acme.validation.booking.no-modify-after-payment");
		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(booking.isDraftMode(), "draftMode", "customer.booking.error.draftMode");
	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCreditCardNibble", "draftMode", "flight");

		SelectChoices travelClassChoices = SelectChoices.from(ClassType.class, booking.getTravelClass());
		dataset.put("travelClass", travelClassChoices);

		super.getResponse().addData(dataset);
	}
}
