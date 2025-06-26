
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

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger;
		passenger = new Passenger();
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.repository.findCustomerById(customerId);

		passenger.setDraftMode(true);
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
		//if (!super.getBuffer().getErrors().hasErrors("passportNumber")) {
		//	Passenger existing = this.repository.findPassengerByPassportNumber(passenger.getPassportNumber());
		//	if (existing != null)
		//		super.state(false, "passportNumber", "customer.passenger.error.passportNumber.duplicate");
		//}
		//if (!super.getBuffer().getErrors().hasErrors("draftMode"))
		//	super.state(passenger.getDraftMode(), "draftMode", "customer.passenger.error.draftMode");
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
			//record.setDraftMode(true);
			this.repository.save(record);
		}

		// Relacionar con Booking si bookingId está presente
		//if (super.getRequest().hasData("bookingId")) {
		//	Integer bookingId = super.getRequest().getData("bookingId", int.class);
		//	Booking booking = this.repository.findBookingById(bookingId);

		//	if (booking != null) {
		//		BookingRecord bp = new BookingRecord();
		//		bp.setBooking(booking);
		//		bp.setPassenger(passenger);
		//		this.repository.save(bp);
		//	}
		//}
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");

		//if (super.getRequest().hasData("bookingId")) {
		//	Integer bookingId = super.getRequest().getData("bookingId", int.class);
		//	dataset.put("bookingId", bookingId);
		//}

		super.getResponse().addData(dataset);
	}

}
