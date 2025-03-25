
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidPhone;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Technician extends AbstractRole {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)
	protected String			licenseNumber;

	@Mandatory
	@ValidPhone
	@Automapped
	protected String			phoneNumber;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	protected String			specialisation;

	@Mandatory
	@Valid
	@Automapped
	protected Boolean			healthTestPassed;

	@Mandatory
	@ValidNumber
	@Automapped
	protected Integer			yearsOfExperience;

	@Optional
	@ValidString
	@Automapped
	protected String			certifications;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
