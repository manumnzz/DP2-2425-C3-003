
package acme.entities.S3;

import java.util.List;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CrewMemberDashboard extends AbstractEntity {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString
	@Automapped
	private List<String>		lastFiveDestinations;

	@Mandatory
	@ValidNumber
	@Automapped
	private List<Integer>		incidentSeverity;

	@Mandatory
	@ValidString
	@Automapped
	private List<String>		lastCrewMembers;

	@Mandatory
	@ValidNumber
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
