
package acme.features.customer.booking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S2.Booking;
import acme.entities.S2.ClassType;
import acme.features.customer.bookingPassenger.CustomerBookingPassengerRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository			repository;

	@Autowired
	private CustomerBookingPassengerRepository	repositoryBP;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.findBookingById(bookingId);

		boolean status = booking != null && super.getRequest().getPrincipal().hasRealm(booking.getCustomer());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.findBookingById(bookingId);

		System.out.println("bookingId = " + bookingId);
		// Importante: Chequear si el booking existe antes de añadirlo al buffer
		if (booking != null)
			super.getBuffer().addData(booking);
		else
			// Opción 1: Lanzar excepción controlada (recomendado)
			throw new RuntimeException("Booking with id " + bookingId + " not found");
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCreditCardNibble", "draftMode", "flight");

		Money costPerPassenger = booking.getFlight().getCost();
		int passengerCount = this.repositoryBP.findPublishedByBookingId(booking.getId()).size();

		Money totalPrice = new Money();
		totalPrice.setAmount(costPerPassenger.getAmount() * passengerCount);
		totalPrice.setCurrency(costPerPassenger.getCurrency());
		SelectChoices travelClassChoices = SelectChoices.from(ClassType.class, booking.getTravelClass());
		dataset.put("travelClass", travelClassChoices);
		dataset.put("id", booking.getId());
		dataset.put("price", totalPrice);
		super.addPayload(dataset, booking, "customer.identifier");
		super.getResponse().addData(dataset);
	}
}
