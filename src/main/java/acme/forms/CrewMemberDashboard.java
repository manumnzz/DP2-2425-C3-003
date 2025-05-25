
package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrewMemberDashboard extends AbstractForm {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------


	private List<String>		lastFiveDestinations;

	private List<Integer>		incidentSeverity;

	private List<String>		lastCrewMembers;

	private List<Integer>		assignmentsStatus;

	private Double				averageMonthly;
	private Integer				minMonthly;
	private Integer				maxMonthly;
	private Double				stdDevMonthly;
}
