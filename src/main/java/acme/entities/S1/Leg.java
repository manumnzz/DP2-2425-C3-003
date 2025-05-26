
package acme.entities.S1;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.Aircraft;
import acme.entities.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "flightNumber"), @Index(columnList = "flight_id"), @Index(columnList = "flight_id, id"), @Index(columnList = "aircraft_id"), @Index(columnList = "aircraft_id, id"), @Index(columnList = "scheduledDeparture"),
	@Index(columnList = "scheduledArrival"), @Index(columnList = "flight_id, scheduledDeparture"), @Index(columnList = "flight_id, scheduledArrival")
})
public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}\\d{4}$")
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

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public double getDuration() {
		// Verificar que ambos aeropuertos estén asignados
		if (this.departureAirport != null && this.arrivalAirport != null && this.scheduledDeparture != null && this.scheduledArrival != null) {

			// Cálculo de la duración solo si ambos aeropuertos y las fechas están definidas
			long differenceInMillis = this.scheduledArrival.getTime() - this.scheduledDeparture.getTime();
			return (double) differenceInMillis / (1000 * 60 * 60);  // Duración en horas
		}
		return 0;  // O cualquier valor predeterminado si no se cumple la condición
	}

	// Relationships ----------------------------------------------------------


	@Mandatory()
	@Valid()
	@ManyToOne(optional = false)
	private Flight		flight;

	@Mandatory()
	@Valid()
	@ManyToOne(optional = false)
	private Airport		departureAirport;

	@Mandatory()
	@Valid()
	@ManyToOne(optional = false)
	private Airport		arrivalAirport;

	@Mandatory()
	@Valid()
	@ManyToOne(optional = false)
	private Aircraft	aircraft;
}
