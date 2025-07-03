
package acme.features.customer.dashboard;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S2.Booking;
import acme.entities.S2.ClassType;
import acme.forms.CustomerDashboard;
import acme.realms.Customer;

@GuiService
public class CustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void load() {
		Integer customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Booking> bookings = this.repository.findAllBookings(customerId);

		for (Booking b : bookings) {
			System.out.println("Booking ID: " + b.getId());
			System.out.println("  PurchaseMoment: " + b.getPurchaseMoment());
			System.out.println("  Price: " + (b.getPrice() != null ? b.getPrice().toString() : "null"));
			System.out.println("  NumPassengers: " + b.getNumberOfPassengers());
		}

		//Si no hay bookings se asigna un dashboard vacio
		if (bookings.isEmpty() || bookings.equals(null)) {
			System.out.println("El cliente: " + customerId + " no tiene asignado bookings");
			CustomerDashboard dashboardVacio = new CustomerDashboard();
			super.getBuffer().addData(dashboardVacio);
			return;
		}

		String currency = bookings.stream().filter(b -> b.getPrice() != null).findFirst().map(b -> b.getPrice().getCurrency()).orElse("USD");

		Integer anioActual = MomentHelper.getCurrentMoment().getYear();
		List<Booking> lastFiveYearsBookings = bookings.stream().filter(booking -> booking.getPurchaseMoment().getYear() > anioActual - 5).toList();
		int totalFiveYearsBookings = lastFiveYearsBookings.isEmpty() ? 1 : lastFiveYearsBookings.size();

		CustomerDashboard dashboard = new CustomerDashboard();

		//Ultimos 5 destinos
		Collection<String> lastFiveDestinations = bookings.stream().sorted(Comparator.comparing(Booking::getPurchaseMoment).reversed()).map(b -> b.getFlight().getDestinationCity()).distinct().limit(5).toList();
		dashboard.setLastFiveDestinations((List<String>) lastFiveDestinations);

		//Dinero gastado en el ultimo año
		Double total = bookings.stream().filter(booking -> booking.getPurchaseMoment() != null && booking.getPrice() != null).filter(booking -> booking.getPurchaseMoment().getYear() > anioActual - 1).map(booking -> booking.getPrice().getAmount())
			.reduce(0.0, Double::sum);
		Money spent = new Money();
		spent.setAmount(total != null ? total : 0.0);
		spent.setCurrency(currency);
		dashboard.setMoneyInBookingsLastYear(spent);

		//Numero de reservas
		int economyBookings = (int) bookings.stream().filter(b -> b.getTravelClass().equals(ClassType.ECONOMY)).count();
		dashboard.setTotalEconomyBookings(economyBookings);

		int businessBookings = (int) bookings.stream().filter(b -> b.getTravelClass().equals(ClassType.BUSINESS)).count();
		dashboard.setTotalBusinessBookings(businessBookings);

		//Costes ultimos cinco años
		Money bookingTotalCost = new Money();
		bookingTotalCost.setAmount(lastFiveYearsBookings.stream().map(b -> b.getPrice() != null ? b.getPrice().getAmount() : 0.0).reduce(0.0, Double::sum));

		bookingTotalCost.setCurrency(currency);
		dashboard.setCountCostBookingLastYear(bookingTotalCost);

		Money bookingAverageCost = new Money();
		bookingAverageCost.setAmount(bookingTotalCost.getAmount() / totalFiveYearsBookings);
		bookingAverageCost.setCurrency(currency);
		dashboard.setAverageCostBookingLastYear(bookingAverageCost);

		Money bookingMinimumCost = new Money();
		bookingMinimumCost.setAmount(lastFiveYearsBookings.stream().map(b -> b.getPrice() != null ? b.getPrice().getAmount() : null).filter(Objects::nonNull).min(Double::compare).orElse(0.0));

		bookingMinimumCost.setCurrency(currency);
		dashboard.setMinimumCostBookingLastYear(bookingMinimumCost);

		Money bookingMaximumCost = new Money();
		bookingMaximumCost.setAmount(lastFiveYearsBookings.stream().map(b -> b.getPrice() != null ? b.getPrice().getAmount() : null).filter(Objects::nonNull).max(Double::compare).orElse(0.0));
		bookingMaximumCost.setCurrency(currency);
		dashboard.setMaximumCostBookingLastYear(bookingMaximumCost);

		Money bookingDeviationCost = new Money();
		Double varianceCost = lastFiveYearsBookings.stream().map(b -> b.getPrice() != null ? b.getPrice().getAmount() : null).filter(Objects::nonNull).mapToDouble(p -> Math.pow(p - bookingAverageCost.getAmount(), 2)).sum() / totalFiveYearsBookings;
		Double deviationCost = Math.sqrt(varianceCost);
		bookingDeviationCost.setAmount(deviationCost);
		bookingDeviationCost.setCurrency(currency);
		dashboard.setStandardDeviaCostBookingLastYear(bookingDeviationCost);

		//Pasajeros ultimos cinco años
		Integer totalPassengers = lastFiveYearsBookings.stream().map(Booking::getNumberOfPassengers).reduce(0, Integer::sum);
		dashboard.setCountPassengers(totalPassengers);

		double averagePassengers = (double) totalPassengers / totalFiveYearsBookings;
		dashboard.setAveragePassengers(averagePassengers);

		Integer minPassengers = lastFiveYearsBookings.stream().map(Booking::getNumberOfPassengers).min(Integer::compare).orElse(0);
		dashboard.setMinimumPassengers(minPassengers);

		Integer maxPassengers = lastFiveYearsBookings.stream().map(Booking::getNumberOfPassengers).max(Integer::compare).orElse(0);
		dashboard.setMaximumPassengers(maxPassengers);

		double varianzaPassengers = lastFiveYearsBookings.stream().map(Booking::getNumberOfPassengers).mapToDouble(x -> Math.pow(x - averagePassengers, 2)).sum() / totalFiveYearsBookings;
		double deviationPassengers = Math.sqrt(varianzaPassengers);
		dashboard.setStandardDeviationPassengers(deviationPassengers);

		super.getBuffer().addData(dashboard);

	}

	@Override
	public void unbind(final CustomerDashboard object) {
		Dataset dataset = super.unbindObject(object, "lastFiveDestinations", "moneyInBookingsLastYear", "totalEconomyBookings", "totalBusinessBookings", "countCostBookingLastYear", "averageCostBookingLastYear", "minimumCostBookingLastYear",
			"maximumCostBookingLastYear", "standardDeviaCostBookingLastYear", "countPassengers", "averagePassengers", "minimumPassengers", "maximumPassengers", "standardDeviationPassengers");

		super.getResponse().addData(dataset);
	}
}
