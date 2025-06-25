
package acme.features.customer.booking;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S1.Flight;
import acme.entities.S1.Leg;
import acme.entities.S2.Booking;
import acme.entities.S2.ClassType;
import acme.features.customer.bookingRecord.CustomerBookingRecordRepository;
import acme.realms.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository			repository;

	@Autowired
	private CustomerBookingRecordRepository	repositoryBP;

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
		//else

		//throw new RuntimeException("Booking with id " + bookingId + " not found");
	}

	@Override
	public void unbind(final Booking booking) {
		// Construcción de la lista de vuelos para el combo
		Collection<Flight> flights = this.repository.findAllFlights();
		SelectChoices flightChoices = new SelectChoices();

		for (Flight flight : flights) {
			List<Leg> legs = this.repository.findByFlightIdOrdered(flight.getId());
			if (!legs.isEmpty()) {
				Leg firstLeg = legs.get(0);
				Leg lastLeg = legs.get(legs.size() - 1);

				String origin = firstLeg.getDepartureAirport().getIataCode();
				String destination = lastLeg.getArrivalAirport().getIataCode();
				String departure = firstLeg.getScheduledDeparture().toString();
				String arrival = lastLeg.getScheduledArrival().toString();

				String label = origin + " – " + destination + " (" + departure + " – " + arrival + ")";
				boolean selected = booking.getFlight() != null && booking.getFlight().getId() == flight.getId();
				flightChoices.add(String.valueOf(flight.getId()), label, selected);
			}
		}

		Dataset dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCreditCardNibble", "draftMode", "flight");
		dataset.put("flights", flightChoices);

		// Manejo seguro de flight y cost
		Money costPerPassenger = new Money();
		if (booking.getFlight() != null)
			costPerPassenger = booking.getFlight().getCost();

		// Número de pasajeros publicados
		int passengerCount = 0;
		try {
			passengerCount = this.repositoryBP.findPublishedByBookingId(booking.getId()).size();
		} catch (Exception e) {
			passengerCount = 0;
		}

		// Precio total
		Money totalPrice = new Money();
		totalPrice.setAmount(costPerPassenger.getAmount() * passengerCount);
		totalPrice.setCurrency(costPerPassenger.getCurrency());
		dataset.put("price", totalPrice);

		// travelClass como SelectChoices
		SelectChoices travelClassChoices = SelectChoices.from(ClassType.class, booking.getTravelClass());
		dataset.put("travelClass", travelClassChoices);

		// lastCreditCardNibble como String o vacío
		dataset.put("lastCreditCardNibble", booking.getLastCreditCardNibble() != null ? booking.getLastCreditCardNibble() : "");

		// purchaseMoment con formato
		String formattedMoment = "";
		if (booking.getPurchaseMoment() != null)
			formattedMoment = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm").format(booking.getPurchaseMoment());
		dataset.put("purchaseMoment", formattedMoment);

		// id y version
		dataset.put("id", booking.getId());
		dataset.put("version", booking.getVersion());

		super.addPayload(dataset, booking, "customer.identifier");
		super.getResponse().addData(dataset);
	}

}
