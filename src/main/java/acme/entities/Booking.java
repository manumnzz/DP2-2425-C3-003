
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,8}$")
	@Automapped
	protected String			locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	protected Date				purchaseMoment;

	@Mandatory
	@Valid
	@Automapped
	protected ClassType			travelClass;

	@Mandatory
	@ValidMoney
	@Automapped
	protected Money				price;

	@Optional
	@ValidString(pattern = "^\\d{4}$")
	@Automapped
	protected String			lastCreditCardNibble;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	protected Customer			customer;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	protected Flight			fligth;
}
