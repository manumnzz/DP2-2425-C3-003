
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.checkerframework.common.aliasing.qual.Unique;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@Unique
	@NotBlank(message = "La etiqueta es obligatoria")
	@Size(max = 50, message = "La etiqueta no puede superar los 50 caracteres")
	@Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "La etiqueta solo puede contener letras y números")
	@Column(unique = true, nullable = false)
	private String				etiqueta;

	@Mandatory
	@Valid
	@Automapped
	@NotNull(message = "Debe especificar si requiere self-transfer")
	@Column(nullable = false)
	private Boolean				requiereSelfTransfer;

	@Mandatory
	@Valid
	@Automapped
	@DecimalMin(value = "10.0", message = "El costo mínimo permitido es 10.0")
	@Column(nullable = false)
	private Double				costo;

	@Optional
	@Valid
	@Automapped
	@Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
	@Column(nullable = true)
	private String				descripcion;

	@Mandatory
	@Valid
	@Automapped
	@OneToMany(mappedBy = "vuelo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Leg>			legs;

	@Mandatory
	@Valid
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	@Column(nullable = false, updatable = false)
	private Date				fechaCreacion;

	@Mandatory
	@Valid
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	@Column(nullable = false)
	private Date				fechaActualizacion;
}
