
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private List<String>		lastFiveDestinations;
	private Money				moneyInBookingsLastYear;
	private Integer				totalEconomyBookings;
	private Integer				totalBusinessBookings;
	private Money				countCostBookingLastYear;
	private Money				averageCostBookingLastYear;
	private Money				minimumCostBookingLastYear;
	private Money				maximumCostBookingLastYear;
	private Money				standardDeviaCostBookingLastYear;

	private Integer				countPassengers;
	private Double				averagePassengers;
	private Integer				minimumPassengers;
	private Integer				maximumPassengers;
	private Double				standardDeviationPassengers;
}
