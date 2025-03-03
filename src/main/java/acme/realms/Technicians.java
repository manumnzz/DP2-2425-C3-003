
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.client.components.basis.AbstractRole;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Technicians extends AbstractRole {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$")
	protected String			licenseNumber;

	@Pattern(regexp = "^\\+?\\d{6,15}$")
	protected String			phoneNumber;

	@NotBlank
	@Length(max = 50)
	protected String			specialization;

	protected boolean			healthTestPassed;

	protected Integer			yearsExperience;

	@NotBlank
	@Length(max = 255)
	protected String			certifications;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
