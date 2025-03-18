
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.entities.S1.Leg;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomersDashboards extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	List<Leg>					lastFiveDestinations;
	Money						moneyInBookingsLastYear;
	int							totalBookings;

	Double						countCostBookingLastYear;
	Double						averageCostBookingLastYear;
	Double						minimumCostBookingLastYear;
	Double						maximumCostBookingLastYear;
	Double						standardDerivCostBookingLastYear;

	Double						countPassengers;
	Double						averagePassengers;
	Double						minimumPassengers;
	Double						maximumPassengers;
	Double						standardDerivPassengers;
}
