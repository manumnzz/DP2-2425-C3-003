
package acme.entities.aircraft;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidShortText;
import acme.entities.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Aircraft extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidShortText
	@Automapped
	protected String			model;

	@Mandatory
	@ValidShortText
	@Column(unique = true)
	protected String			registrationNumber;

	@Mandatory
	@ValidNumber
	@Automapped
	protected Integer			capacity;

	@Mandatory
	@ValidNumber(min = 20, max = 50)
	@Automapped
	protected Integer			cargoWeight;

	@Mandatory
	@Valid
	@Automapped
	protected AircraftStatus	status;

	@Optional
	@ValidString
	@Automapped
	protected String			optionalDetails;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

}
