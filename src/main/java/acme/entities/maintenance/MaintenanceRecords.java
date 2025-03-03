
package acme.entities.maintenance;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.realms.Technicians;

public class MaintenanceRecords extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	protected Date				moment;

	protected MaintenanceStatus	status;

	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	protected Date				nextInspection;

	@ValidMoney
	protected Money				cost;

	@NotBlank
	@Length(max = 255)
	protected String			notes;
	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne
	private Technicians			technician;

}
