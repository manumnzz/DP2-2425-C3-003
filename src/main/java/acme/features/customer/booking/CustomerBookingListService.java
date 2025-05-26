
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S2.Booking;
import acme.entities.S2.ClassType;
import acme.features.customer.bookingPassenger.CustomerBookingPassengerRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingListService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository			repository;

	@Autowired
	private CustomerBookingPassengerRepository	repositoryBP;

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

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCreditCardNibble", "draftMode", "flight");

		Money costPerPassenger = booking.getFlight().getCost();
		int passengerCount = this.repositoryBP.findPublishedByBookingId(booking.getId()).size();

		Money totalPrice = new Money();
		totalPrice.setAmount(costPerPassenger.getAmount() * passengerCount);
		totalPrice.setCurrency(costPerPassenger.getCurrency());
		SelectChoices travelClassChoices = SelectChoices.from(ClassType.class, booking.getTravelClass());
		dataset.put("travelClass", travelClassChoices);
		dataset.put("price", totalPrice);
		super.addPayload(dataset, booking, "customer.identifier");
		super.getResponse().addData(dataset);
	}
}
