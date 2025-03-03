
package acme.entities.maintenance;

import java.time.Duration;

import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.realms.Technicians;

public class Task extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Valid
	@Automapped
	protected TaskType			type;

	@Mandatory
	@ValidString
	@Automapped
	protected String			description;

	@Mandatory
	@ValidNumber(max = 10)
	@Automapped
	protected Integer			priority;

	@Mandatory
	@Valid
	@Automapped
	protected Duration			duration;
	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Technicians			technician;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private MaintenanceRecords	maintenanceRecord;

}
