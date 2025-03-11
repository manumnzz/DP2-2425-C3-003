
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Positive;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				origen;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				destino;

	@Mandatory
	@Valid
	@FutureOrPresent(message = "La fecha de salida debe ser presente o futura")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date				fechaSalida;

	@Mandatory
	@FutureOrPresent(message = "La fecha de llegada debe ser futura")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				fechaLlegada;

	@Mandatory
	@Valid
	@Positive(message = "La duración debe ser un número positivo")
	private Double				duracionHoras;

	@Optional
	@ValidString(max = 50)
	@Automapped
	private String				comentarios;

	@Mandatory
	@Valid
	@ManyToOne
	private Flight				vuelo;
}
