
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S2.Passenger;
import acme.features.customer.bookingRecord.CustomerBookingRecordRepository;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerUpdateService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository		repository;

	@Autowired
	private CustomerBookingRecordRepository	bookingPassengerRepository;


	@Override
	public void authorise() {
		int passengerId = super.getRequest().getData("id", int.class);
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		Passenger passenger = this.repository.findPassengerById(passengerId);

		boolean status = false;
		if (passenger != null)
			// Comprueba que pertenece al customer y que está en borrador
			status = this.bookingPassengerRepository.existsBookingPassengerForCustomer(passengerId, customer.getId()) > 0;
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
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
	}

	@Override
	public void validate(final Passenger passenger) {
		if (!super.getBuffer().getErrors().hasErrors("passportNumber")) {
			Passenger existing = this.repository.findPassengerByPassportNumber(passenger.getPassportNumber());
			// No es duplicado si es el mismo pasajero
			if (existing != null && existing.getId() != passenger.getId())
				super.state(false, "passportNumber", "customer.passenger.error.passportNumber.duplicate");
		}
		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(passenger.getDraftMode(), "draftMode", "customer.passenger.error.draftMode");
	}

	@Override
	public void perform(final Passenger passenger) {
		this.repository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);
	}
}
