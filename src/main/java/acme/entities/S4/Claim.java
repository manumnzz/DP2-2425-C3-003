
package acme.entities.S4;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidLongText;
import acme.entities.S1.Leg;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Claim extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@ValidMoment(past = true)
	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Mandatory
	@ValidEmail
	@Length(min = 0, max = 255)
	private String				passengerEmail;

	@ValidLongText
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
	public ClaimStatus getAccepted() {
		TrackingLogRepository repository = SpringHelper.getBean(TrackingLogRepository.class);
		List<TrackingLog> listLastTr = repository.findLatestTrackingLogPublishedByClaim(this.getId());
		TrackingLog lastTr;
		ClaimStatus res = ClaimStatus.PENDING;

		if (listLastTr.isEmpty())
			lastTr = null;
		else
			lastTr = listLastTr.get(0);

		if (lastTr == null) {
		} else if (lastTr.getStatus() == TrackingLogStatus.ACCEPTED)
			res = ClaimStatus.ACCEPTED;
		else if (lastTr.getStatus() == TrackingLogStatus.REJECTED)
			res = ClaimStatus.REJECTED;

		return res;
	}
}
