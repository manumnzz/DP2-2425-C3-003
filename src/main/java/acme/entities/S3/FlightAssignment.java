
package acme.entities.S3;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.S1.Leg;
import acme.realms.FlightCrewMembers;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightAssignment extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private FlightCrew			flightCrew;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private CurrentStatus		currentStatus;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				remarks;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne
	private FlightCrewMembers	crewMember;

	@Mandatory
	@Valid
	@ManyToOne
	private Leg					leg;
}
