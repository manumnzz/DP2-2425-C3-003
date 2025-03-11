
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Passenger extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 255)
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
	@ValidString(max = 50)
	@Optional
	protected String			specialNeeds;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
