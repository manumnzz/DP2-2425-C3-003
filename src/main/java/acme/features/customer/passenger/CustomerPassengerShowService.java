
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S2.Passenger;
import acme.features.customer.bookingRecord.CustomerBookingRecordRepository;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerShowService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository			repository;

	@Autowired
	private CustomerBookingRecordRepository	bookingPassengerRepository;


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
	public void unbind(final Passenger passenger) {
		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode", "id", "version");

		super.getResponse().addData(dataset);
	}

}
