
package acme.entities.S2;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
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
	@ValidString(pattern = "^[A-Z0-9]{6,9}$")
	@Column(unique = true)
	protected String			passportNumber;

	@Mandatory
	@ValidLongText
	@Automapped
	protected String			fullName;

	@Mandatory
	@ValidString
	@Automapped
	protected String			email;

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

}
