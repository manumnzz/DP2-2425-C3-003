
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S2.BookingRecord;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordListService extends AbstractGuiService<Customer, BookingRecord> {

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
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<BookingRecord> bps = this.repository.findAllBookingPassengerByCustomerId(customerId);
		super.getBuffer().addData(bps);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;
		dataset = super.unbindObject(bookingRecord, "booking", "passenger");
		dataset.put("bookingLocCode", bookingRecord.getBooking().getLocatorCode());
		dataset.put("passengerFullName", bookingRecord.getPassenger().getFullName());
		super.getResponse().addData(dataset);
	}

}
