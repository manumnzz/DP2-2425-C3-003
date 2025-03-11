
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
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AssistanceAgent extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	@Mandatory
	@Automapped
	private String				code;

	@ValidString(min = 0, max = 255)
	@Mandatory
	@Automapped
	private String				spokenLanguages;

	@ValidString(min = 0, max = 50)
	@Mandatory
	@Automapped
	private String				airline;

	@ValidMoment(past = true)
	@Mandatory
	@Automapped
	private Date				moment;

	@ValidString(min = 0, max = 255)
	@Optional
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
