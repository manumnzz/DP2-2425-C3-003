
package acme.entities.S3;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;

public class FlightAssignment {

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
	@Column(unique = true)
	private Date				moment;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private CurrentStatus		currentStatus;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				remarks;


	//Enum for flight crew 
	public enum FlightCrew {
		PILOT, COPILOT, LEAD_ATTENDANT, CABIN_ATTENDANT
	}

	//Enum for current status
	public enum CurrentStatus {
		CONFIRMED, PENDING, CANCELLED
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@ManyToOne
	private FlightCrewMembers crewMember;
}
