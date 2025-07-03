
package acme.features.customer.bookingRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S2.BookingRecord;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordShowService extends AbstractGuiService<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Customer customer;
		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		Integer bookingRecordId = super.getRequest().getData("id", int.class);
		BookingRecord bookingRecord = this.repository.getBookingRecordByBookingRecordId(bookingRecordId);

		status = customer != null && bookingRecord.getBooking().getCustomer().getId() == customer.getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		BookingRecord bookingRecord;
		int bookingRecordId;

		bookingRecordId = super.getRequest().getData("id", int.class);
		bookingRecord = this.repository.getBookingRecordByBookingRecordId(bookingRecordId);
		super.getBuffer().addData(bookingRecord);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Boolean publishedBooking = bookingRecord.getBooking().getDraftMode();
		Dataset dataset = super.unbindObject(bookingRecord, "booking", "passenger");
		dataset.put("bookingLocator", bookingRecord.getBooking().getLocatorCode());
		dataset.put("passengerFullName", bookingRecord.getPassenger().getFullName());
		dataset.put("publishedBooking", publishedBooking);
		super.getResponse().addData(dataset);
	}

}
