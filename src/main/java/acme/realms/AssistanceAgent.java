
package acme.realms;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidLongText;
import acme.entities.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AssistanceAgent extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Mandatory
	@Automapped
	private String				code;

	@Mandatory
	@ValidLongText
	@Automapped
	private String				spokenLanguages;

	@ValidMoment(past = true)
	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Optional
	@ValidLongText
	@Automapped
	private String				biography;

	@ValidMoney
	@Optional
	@Automapped
	private Money				salary;

	@ValidUrl
	@Optional
	@Automapped
	private String				photo;

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Airline				airline;
}
