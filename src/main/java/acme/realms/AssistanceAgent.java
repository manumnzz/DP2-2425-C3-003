
package acme.realms;

import java.util.Date;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
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

	@Mandatory
	@ValidShortText
	@Automapped
	private String				airline;

	@ValidMoment(past = true)
	@Mandatory
	@Automapped
	private Date				moment;

	@Optional
	@ValidLongText
	@Automapped
	private String				biography;

	@ValidNumber(max = 10000)
	@Optional
	@Automapped
	private Integer				salary;

	@ValidUrl
	@Optional
	@Automapped
	private String				photo;
}
