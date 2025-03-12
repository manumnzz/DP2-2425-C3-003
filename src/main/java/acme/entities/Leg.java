
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Valid
	@Column(unique = true)
	private String				flightNumber;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Valid
	@Automapped
	private FlightStatus		status;

	// Derived attributes -----------------------------------------------------


	@Transient
	public double getDuration() {
		long differenceInMillis = this.scheduledArrival.getTime() - this.scheduledDeparture.getTime();

		double durationInHours = (double) differenceInMillis / (1000 * 60 * 60);

		return durationInHours;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne
	private Flight		flight;

	@Mandatory
	@Valid
	@ManyToOne
	private Airport		departureAirport;

	@Mandatory
	@Valid
	@ManyToOne
	private Airport		arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne
	private Aircraft	aircraft;
}
