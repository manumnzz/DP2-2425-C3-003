
package acme.entities.S1;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.entities.Airport;
import acme.realms.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ManagerDashboard extends AbstractEntity {

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
