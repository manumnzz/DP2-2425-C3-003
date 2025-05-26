
package acme.features.customer.booking;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
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
		booking.setCustomer(customer);
		booking.setDraftMode(true);
		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCreditCardNibble", "draftMode", "flight");
	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void validate(final Booking booking) {
		if (!super.getBuffer().getErrors().hasErrors("locatorCode")) {
			Booking existing = this.repository.findBookingByLocatorCode(booking.getLocatorCode());
			if (existing != null && existing.getId() != booking.getId())
				super.state(false, "locatorCode", "customer.booking.error.locatorCode.duplicate");
		}
		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(booking.isDraftMode(), "draftMode", "customer.booking.error.draftMode");
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCreditCardNibble", "draftMode", "flight");
		//Collection<Flight> flights = this.fl
		Money price = new Money();
		price.setAmount(0.0);
		price.setCurrency("USD");
		SelectChoices travelClassChoices = SelectChoices.from(ClassType.class, booking.getTravelClass());
		dataset.put("travelClass", travelClassChoices);
		dataset.put("price", price);
		super.getResponse().addData(dataset);
	}
}
