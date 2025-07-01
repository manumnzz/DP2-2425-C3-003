
package acme.entities.S4;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Past;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidTrackingLog;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidTrackingLog
@Table(indexes = {
	@Index(columnList = "claim_id")
})
public class TrackingLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Past
	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	private Date				updateTime;

	@Mandatory
	@ValidString(min = 1, max = 50) // está asi y no @validShotText porque aparece {acme.validation.text.message} y no lo puedo quitar
	@Automapped
	private String				step;

	@Mandatory
	@ValidScore
	@Automapped
	private Double				resolutionPercentage;

	@Mandatory
	@Valid
	@Automapped
	private TrackingLogStatus	status;

	@Optional
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				resolution;

	@Mandatory
	@Automapped
	@Valid
	private Boolean				draftMode;

	//Relations --------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Claim				claim;

}
