
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S2.Booking;
import acme.entities.S2.BookingRecord;
import acme.entities.S2.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerCreateService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		boolean status = false;

		if (super.getRequest().getData().containsKey("bookingId")) {
			int bookingId = super.getRequest().getData("bookingId", int.class);
			Booking booking = this.repository.findBookingById(bookingId);
			Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

			// Solo si el booking existe, es del mismo customer, y está en modo borrador
			status = booking != null && booking.getCustomer().getId() == customer.getId() && Boolean.TRUE.equals(booking.getDraftMode());
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger = new Passenger();
		passenger.setDraftMode(true);

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.repository.findCustomerById(customerId);
		passenger.setCustomer(customer);

		super.getBuffer().addData(passenger);

		int bookingId = super.getRequest().getData("bookingId", int.class);
		super.getResponse().addGlobal("bookingId", bookingId);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		if (!super.getBuffer().getErrors().hasErrors("passportNumber")) {
			int count = this.repository.countByCustomerIdAndPassportNumber(passenger.getCustomer().getId(), passenger.getPassportNumber());
			super.state(count == 0, "passportNumber", "acme.validation.passenger.duplicate-passport");
		}
	}

	@Override
	public void perform(final Passenger passenger) {
		passenger.setDraftMode(true);
		this.repository.save(passenger);

		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.repository.findBookingById(bookingId);

		if (booking != null) {
			BookingRecord record = new BookingRecord();
			record.setBooking(booking);
			record.setPassenger(passenger);
			this.repository.save(record);
		}
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);
	}
}
