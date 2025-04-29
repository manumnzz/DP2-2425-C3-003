
package acme.entities.S4;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Past;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrackingLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Past
	@Mandatory
	@Temporal(TemporalType.DATE)
	private Date				updateTime;

	@Mandatory
	@ValidShortText
	private String				step;

	@Mandatory
	@ValidNumber(min = 0, max = 100)
	private Integer				resolutionPercentage;

	private boolean				indicator;

	@Optional
	@ValidLongText
	private String				resolution;

	private boolean				draftMode;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Claim				claim;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AssistanceAgent		assistenceAgent;
}
