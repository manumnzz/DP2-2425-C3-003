
package acme.entities.S4;

import java.beans.Transient;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.S1.Leg;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "assistance_agent_id")
})
public class Claim extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@ValidMoment(past = true)
	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@ValidString(min = 1, max = 255)
	@Mandatory
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private TypeClaim			typeClaim;

	@Mandatory
	@Automapped
	@Valid
	private Boolean				draftMode;

	//Relarions ------------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AssistanceAgent		assistanceAgent;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg					leg;

	//Derivated ------------------------------------------------------------


	@Transient
	public TrackingLogStatus getIndicator() {
		TrackingLogRepository repository = SpringHelper.getBean(TrackingLogRepository.class);
		TrackingLogStatus res = repository.findOrderedTrackingLogs(this.getId()).stream().max(Comparator.comparingDouble(TrackingLog::getResolutionPercentage)).map(TrackingLog::getStatus).orElse(TrackingLogStatus.PENDING);
		return res;
	}
}
