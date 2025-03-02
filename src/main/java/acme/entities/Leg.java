
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.dom4j.tree.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				id;

	@ManyToOne
	@JoinColumn(name = "vuelo_id", nullable = false)
	private Flight				flight;

	@NotBlank(message = "El número de vuelo es obligatorio")
	@Pattern(regexp = "^[A-Z]{2}\\d{4}$", message = "El número de vuelo debe seguir el formato IATA (2 letras + 4 dígitos)")
	@Column(unique = true, nullable = false, length = 6)
	private String				flightNumber;

	@NotNull(message = "La fecha de salida programada es obligatoria")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@NotNull(message = "La fecha de llegada programada es obligatoria")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@NotNull(message = "La duración del tramo es obligatoria")
	@Positive(message = "La duración debe ser un número positivo")
	@Max(value = 24, message = "La duración no puede ser mayor a 24 horas")
	private Double				duration;

	@NotNull(message = "El estado del vuelo es obligatorio")
	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private FlightStatus		status;

	@NotBlank(message = "El aeropuerto de salida es obligatorio")
	@Size(max = 100, message = "El nombre del aeropuerto de salida no puede superar los 100 caracteres")
	private String				departureAirport;

	@NotBlank(message = "El aeropuerto de llegada es obligatorio")
	@Size(max = 100, message = "El nombre del aeropuerto de llegada no puede superar los 100 caracteres")
	private String				arrivalAirport;

	@NotBlank(message = "El modelo de avión es obligatorio")
	@Size(max = 50, message = "El modelo de avión no puede superar los 50 caracteres")
	private String				aircraftModel;
}
