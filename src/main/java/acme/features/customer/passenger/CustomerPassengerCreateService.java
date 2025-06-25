
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
		Integer bookingId = super.getRequest().getData("bookingId", Integer.class);
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		boolean status = false;

		if (customer != null && bookingId != null) {
<<<<<<< Updated upstream
			Booking booking = this.repositoryBP.findBookingById(bookingId);
=======
			Booking booking = this.repository.findBookingById(bookingId);
>>>>>>> Stashed changes
			status = booking != null && booking.getCustomer().getId() == customer.getId() && booking.isDraftMode(); // Solo permite si está en borrador
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger;
		passenger = new Passenger();
		passenger.setDraftMode(true);
		//if (super.getRequest().hasData("bookingId")) 
		//int bookingId = super.getRequest().getData("bookingId", int.class);
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
	}

	@Override
	public void validate(final Passenger passenger) {
		if (!super.getBuffer().getErrors().hasErrors("passportNumber")) {
			Passenger existing = this.repository.findPassengerByPassportNumber(passenger.getPassportNumber());
			if (existing != null)
				super.state(false, "passportNumber", "customer.passenger.error.passportNumber.duplicate");
		}
		//if (!super.getBuffer().getErrors().hasErrors("draftMode"))
		//	super.state(passenger.isDraftMode(), "draftMode", "customer.passenger.error.draftMode");
	}

	@Override
	public void perform(final Passenger passenger) {
		this.repository.save(passenger);

		// Relacionar con Booking si bookingId está presente
		if (super.getRequest().hasData("bookingId")) {
			Integer bookingId = super.getRequest().getData("bookingId", int.class);
<<<<<<< Updated upstream
			Booking booking = this.repositoryBP.findBookingById(bookingId);
=======
			Booking booking = this.repository.findBookingById(bookingId);
>>>>>>> Stashed changes

			if (booking != null) {
				BookingRecord bp = new BookingRecord();
				bp.setBooking(booking);
				bp.setPassenger(passenger);
				this.repository.save(bp);
			}
		}

		super.getBuffer().addData(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");

		if (super.getRequest().hasData("bookingId")) {
			Integer bookingId = super.getRequest().getData("bookingId", int.class);
			dataset.put("bookingId", bookingId);
		}

		super.getResponse().addData(dataset);
	}

}
