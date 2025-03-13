
package acme.realms;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.checkerframework.common.aliasing.qual.Unique;

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
public class AirlineManager extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Unique
	private String				identifier;

	@Mandatory
	@Positive
	@Automapped
	private int					yearsOfExperience;

	@ValidMoment(past = true)
	@Mandatory
	@Temporal(TemporalType.DATE)
	private Date				dateOfBirth;

	@Optional
	@Valid
	@Automapped
	private String				pictureUrl;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
