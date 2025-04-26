
package acme.features.customer.bookingPassenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.services.GuiService;
import acme.entities.S2.BookingPassenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingPassengerListService extends AbstractService<Customer, BookingPassenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingPassengerRepository repository;

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
		Collection<BookingPassenger> bps = this.repository.findAllBookingPassengerByCustomerId(customerId);
		super.getBuffer().addData(bps);
	}

	@Override
	public void unbind(final BookingPassenger bp) {
		Dataset dataset;
		dataset = super.unbindObject(bp, "booking", "passenger");
		super.getResponse().addData(dataset);
	}

}
