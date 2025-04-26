
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S2.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerCreateService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		System.out.println("AUTHORISE");
		boolean status;
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		status = customer != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		System.out.println("LOAD");
		Passenger passenger;
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		passenger = new Passenger();
		passenger.setCustomer(customer);
		passenger.setDraftMode(true);
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		System.out.println("BIND");
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
	}

	@Override
	public void validate(final Passenger passenger) {
		System.out.println("VALIDATE");
		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(passenger.isDraftMode(), "draftMode", "customer.passenger.error.draftMode");
	}

	@Override
	public void perform(final Passenger passenger) {
		System.out.println("Guardando passenger: " + passenger);
		this.repository.save(passenger);
		super.getBuffer().addData(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		System.out.println("UNBIND: " + passenger);
		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
		dataset.put("customer", passenger.getCustomer().getUserAccount().getUsername());
		super.getResponse().addData(dataset);
	}

}
