
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S2.Booking;
import acme.realms.Customer;

@GuiService
public class CustomerBookingListService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		boolean status = customer != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Booking> bookings = this.repository.findBookingsByCustomerId(customerId);
		// Debug: imprime los ids de los bookings cargados
		super.getBuffer().addData(bookings);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset = super.unbindObject(booking, "locatorCode", "travelClass", "lastCreditCardNibble", "draftMode", "flight", "id", "version");

		// Manejo seguro de flight y cost
		Money costPerPassenger = new Money();
		if (booking.getFlight() != null && booking.getFlight().getCost() != null)
			costPerPassenger = booking.getFlight().getCost();

		int passengerCount = 0;
		try {
			passengerCount = this.repository.findPublishedByBookingId(booking.getId()).size();
		} catch (Exception e) {
			passengerCount = 0;
		}

		Money totalPrice = new Money();
		totalPrice.setAmount(costPerPassenger.getAmount() * passengerCount);
		totalPrice.setCurrency(costPerPassenger.getCurrency());
		dataset.put("price", totalPrice);

		String travelClassLabel = booking.getTravelClass() != null ? booking.getTravelClass().toString() : "";
		dataset.put("travelClass", travelClassLabel);

		dataset.put("lastCreditCardNibble", booking.getLastCreditCardNibble() != null ? booking.getLastCreditCardNibble() : "");

		dataset.put("purchaseMoment", booking.getPurchaseMoment());

		dataset.put("id", booking.getId());
		dataset.put("version", booking.getVersion());

		super.addPayload(dataset, booking, "customer.identifier");
		super.getResponse().addData(dataset);
	}

}
