
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
import acme.datatypes.Phone;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractRole {

	//Serialisation version -------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	//Atributes -------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)
	protected String			identifier;

	@Mandatory
	@Valid
	@Automapped
	protected Phone				phoneNumber;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	protected String			physicalAddress;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	protected String			city;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	protected String			country;

	@Optional
	@ValidNumber(max = 500000)
	@Automapped
	protected double			earnedPoints;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
