
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S2.Booking;
import acme.entities.S2.BookingRecord;
import acme.entities.S2.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordCreateService extends AbstractGuiService<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository repository;

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
		BookingRecord bp = new BookingRecord();
		super.getBuffer().addData(bp);
	}

	@Override
	public void bind(final BookingRecord bp) {
		super.bindObject(bp, "booking", "passenger");
	}

	@Override
	public void perform(final BookingRecord bp) {
		if (this.repository.existsBookingPassenger(bp.getBooking().getId(), bp.getPassenger().getId()) == 0)
			this.repository.save(bp);
		else {
			// Retorna error o mensaje de duplicado
		}
	}

	@Override
	public void unbind(final BookingRecord bp) {
		Dataset dataset;
		Customer customer;
		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		SelectChoices bkChoices;
		SelectChoices psChoices;
		Collection<Booking> bookings;
		Collection<Passenger> passengers;
		bookings = this.repository.findBookingByCustomerId(customer.getId());
		passengers = this.repository.findPassengerByCustomerId(customer.getId());
		bkChoices = SelectChoices.from(bookings, "id", bp.getBooking());
		psChoices = SelectChoices.from(passengers, "id", bp.getPassenger());

		dataset = super.unbindObject(bp, "booking", "passenger");
		dataset.put("booking", bkChoices);
		dataset.put("passenger", psChoices);
		super.getResponse().addData(dataset);
	}
}
