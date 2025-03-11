
package acme.entities;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;

import org.checkerframework.common.aliasing.qual.Unique;

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
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Unique
	@Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "La etiqueta solo puede contener letras y números")
	private String				etiqueta;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				requiereSelfTransfer;

	@Mandatory
	@Valid
	@Automapped
	@DecimalMin(value = "10.0", message = "El costo mínimo permitido es 10.0")
	private Double				costo;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				descripcion;

	@Mandatory
	@Valid
	@Automapped
	@OneToMany(mappedBy = "vuelo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Leg>			legs;

	@Mandatory
	@Valid
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, updatable = false)
	private Date				fechaCreacion;

	@Mandatory
	@Valid
	@Temporal(TemporalType.TIMESTAMP)
	private Date				fechaActualizacion;
}
