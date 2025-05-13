
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S2.Booking;
import acme.entities.S2.BookingPassenger;
import acme.entities.S2.Passenger;
import acme.features.customer.bookingPassenger.CustomerBookingPassengerRepository;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerCreateService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository			repository;

	@Autowired
	private CustomerBookingPassengerRepository	repositoryBP;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		status = customer != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger;
		passenger = new Passenger();
		passenger.setDraftMode(true);
		if (super.getRequest().hasData("bookingId")) {
			int bookingId = super.getRequest().getData("bookingId", int.class);
			System.out.println(">>> Booking ID recibido: " + bookingId);
		}
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
	}

	@Override
	public void validate(final Passenger passenger) {
		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(passenger.isDraftMode(), "draftMode", "customer.passenger.error.draftMode");
	}

	@Override
	public void perform(final Passenger passenger) {
		this.repository.save(passenger);

		// Relacionar con Booking si bookingId está presente
		if (super.getRequest().hasData("bookingId")) {
			int bookingId = super.getRequest().getData("bookingId", int.class);
			System.out.println(">>> Asociando pasajero al booking con ID: " + bookingId);
			Booking booking = this.repositoryBP.findBookingById(bookingId);

			if (booking != null) {
				BookingPassenger bp = new BookingPassenger();
				bp.setBooking(booking);
				bp.setPassenger(passenger);
				this.repositoryBP.save(bp);
			}
		}

		super.getBuffer().addData(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");

		if (super.getRequest().hasData("bookingId")) {
			int bookingId = super.getRequest().getData("bookingId", int.class);
			dataset.put("bookingId", bookingId);
		}

		super.getResponse().addData(dataset);
	}

}
