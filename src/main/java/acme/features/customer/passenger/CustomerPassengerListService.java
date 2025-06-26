
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S2.Booking;
import acme.entities.S2.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Booking booking;
		int bookingId;
		if (super.getRequest().getData().containsKey("bookingId")) {
			bookingId = super.getRequest().getData("bookingId", int.class);
			booking = this.repository.findBookingById(bookingId);
			Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
			status = booking != null && booking.getCustomer().getId() == customer.getId();
		} else
			status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Passenger> passengers;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int bookingId;
		if (super.getRequest().getData().containsKey("bookingId")) {
			bookingId = super.getRequest().getData("bookingId", int.class);
			super.getResponse().addGlobal("bookingId", bookingId);
			passengers = this.repository.findPassengersByBookingId(bookingId);
		} else
			passengers = this.repository.findPassengerByCustomerId(customerId);
		System.out.println(passengers);
		super.getBuffer().addData(passengers);

	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
		Boolean containsBookingId = super.getRequest().getData().containsKey("bookingId");
		super.getRequest().addData("containsBookingId", containsBookingId);
		super.getResponse().addData(dataset);
	}

}
