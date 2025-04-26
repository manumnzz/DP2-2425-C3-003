
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S2.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerShowService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		Customer customer;
		int passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);
		customer = passenger == null ? null : passenger.getCustomer();
		boolean status = super.getRequest().getPrincipal().hasRealm(customer) || passenger != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int passengerId = super.getRequest().getData("id", int.class); // <-- ¿seguro que es "id"?
		Passenger passenger = this.repository.findPassengerById(passengerId);
		if (passenger != null)
			super.getBuffer().addData(passenger);
		else
			throw new RuntimeException("Passenger not found");
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
		dataset.put("customer", passenger.getCustomer().getUserAccount().getUsername());
		super.getResponse().addData(dataset);
	}
}
