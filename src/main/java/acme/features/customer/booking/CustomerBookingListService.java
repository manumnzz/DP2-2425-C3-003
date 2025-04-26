
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S2.Booking;
import acme.entities.S2.ClassType;
import acme.realms.Customer;

@GuiService
public class CustomerBookingListService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Customer customer;
		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		status = customer != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Booking> bookings;
		int customerId;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		bookings = this.repository.findBookingsByCustomerId(customerId);

		super.getBuffer().addData(bookings);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCreditCardNibble", "draftMode");

		/*
		 * dataset.put("locatorCode", booking.getLocatorCode());
		 * dataset.put("purchaseMoment", booking.getPurchaseMoment());
		 * dataset.put("travelClass", booking.getTravelClass());
		 * dataset.put("price", booking.getPrice());
		 * dataset.put("locatorCode", booking.getCustomer().getIdentifier());
		 */
		SelectChoices travelClassChoices = SelectChoices.from(ClassType.class, booking.getTravelClass());
		dataset.put("travelClass", travelClassChoices);
		super.getResponse().addData(dataset);
	}
}
