
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidPhone;
import acme.constraints.ValidShortText;
import acme.entities.S2.AirlineType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airline extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidShortText
	@Automapped
	protected String			name;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2}X$")
	@Automapped
	protected String			iataCode;

	@Mandatory
	@ValidUrl
	@Automapped
	protected String			website;

	@Mandatory
	@Valid
	@Automapped
	protected AirlineType		type;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				foundationMoment;

	@Mandatory
	@ValidPhone
	@Automapped
	protected String			phoneNumber;

	@Optional
	@ValidEmail
	@Automapped
	protected String			email;
}
