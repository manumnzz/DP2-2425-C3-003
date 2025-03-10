
package acme.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMin;
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
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				id;

	@NotBlank(message = "La etiqueta es obligatoria")  // No puede estar en blanco
	@Size(max = 50, message = "La etiqueta no puede superar los 50 caracteres") // Longitud máxima
	@Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "La etiqueta solo puede contener letras y números") // Solo letras y números
	private String				etiqueta;

	@NotNull(message = "Debe especificar si requiere self-transfer")  // No puede ser nulo
	private Boolean				requiereSelfTransfer;

	@NotNull(message = "El costo no puede ser nulo")
	@Positive(message = "El costo debe ser un valor positivo")  // Solo valores positivos
	@DecimalMin(value = "10.0", message = "El costo mínimo permitido es 10.0") // Mínimo valor permitido
	private Double				costo;

	@Size(max = 255, message = "La descripción no puede superar los 255 caracteres")  // Longitud máxima
	private String				descripcion;

	// Un vuelo tiene múltiples tramos (legs)
	@OneToMany(mappedBy = "vuelo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Leg>			legs;
}
