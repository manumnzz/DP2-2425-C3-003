
package acme.entities.S2;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Passenger extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidLongText
	@Column(unique = true)
	protected String			fullName;

	@Mandatory
	@ValidString
	@Automapped
	protected String			email;

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,9}$")
	@Automapped
	protected String			passportNumber;

	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	protected Date				dateOfBirth;

	@Mandatory
	@ValidShortText
	@Optional
	protected String			specialNeeds;

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	protected boolean			draftMode;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer			customer;
}
