
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrewMemberDashboard extends AbstractForm {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	private List<String>		lastFiveDestinations;

	@Mandatory
	@Automapped
	private List<Integer>		incidentSeverity;

	@Mandatory
	@Automapped
	private List<String>		lastCrewMembers;

	@Mandatory
	@Automapped
	private List<Integer>		assignmentsStatus;

	@Mandatory
	@ValidNumber
	@Automapped
	private Double				averageMonthly;

	@Mandatory
	@ValidNumber
	@Automapped
	private Integer				minMonthly;

	@Mandatory
	@ValidNumber
	@Automapped
	private Integer				maxMonthly;

	@Mandatory
	@ValidNumber
	@Automapped
	private Double				stdDevMonthly;
}
