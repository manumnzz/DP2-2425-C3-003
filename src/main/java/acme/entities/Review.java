
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidShortText
	@NotBlank
	private String				name;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				moment;

	@Mandatory
	@ValidShortText
	@NotBlank
	private String				subject;

	@Mandatory
	@ValidLongText
	@NotBlank
	private String				text;

	@ValidNumber(min = 0.0, max = 10.0)
	@NotNull
	private Double				score;

	@Mandatory
	private boolean				recommended;

	//si faltan o sobran validaciones preguntar cuales

}
