
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.Airport;
import acme.realms.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	List<AirlineManager>			ranking;

	Map<AirlineManager, Integer>	yearsToRetire;

	double							onTimeVsDelayedRatio;

	List<Airport>					airportPopularity;

	Map<String, Integer>			flightsGroupedByStatus;

	double							averageCost;

	double							minCost;

	double							maxCost;

	double							stdDevCost;
}
