
package acme.entities.maintenance;

import java.time.Duration;

import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.realms.Technicians;

public class Task extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	protected TaskType			type;

	@NotBlank
	@ValidString
	@Length(max = 255)
	protected String			description;

	@NotNull
	@Min(0)
	@Max(10)
	@ValidNumber
	protected Integer			priority;

	@NotNull
	protected Duration			duration;
	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne
	private Technicians			technician;

	@Mandatory
	@Valid
	@ManyToOne
	private MaintenanceRecords	maintenanceRecord;

}
