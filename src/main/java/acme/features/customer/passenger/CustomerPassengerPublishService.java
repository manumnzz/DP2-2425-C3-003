
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S2.Passenger;
import acme.features.customer.bookingPassenger.CustomerBookingPassengerRepository;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerPublishService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository			repository;

	@Autowired
	private CustomerBookingPassengerRepository	bookingPassengerRepository;


	@Override
	public void authorise() {
		int passengerId = super.getRequest().getData("id", int.class);
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		boolean status = this.bookingPassengerRepository.existsBookingPassengerForCustomer(passengerId, customer.getId()) > 0;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
	}

	@Override
	public void validate(final Passenger passenger) {
		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(passenger.isDraftMode(), "draftMode", "customer.passenger.error.draftMode");
	}

	@Override
	public void perform(final Passenger passenger) {
		passenger.setDraftMode(false);
		this.repository.save(passenger);
	}

}
