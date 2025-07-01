
package acme.features.assistanceagent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S1.Leg;
import acme.entities.S4.Claim;
import acme.entities.S4.TrackingLog;
import acme.entities.S4.TypeClaim;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimDeleteService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int claimId;
		Claim claim;
		AssistanceAgent assistanceAgent;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);
		assistanceAgent = claim == null ? null : claim.getAssistanceAgent();
		status = claim != null && claim.getDraftMode() && super.getRequest().getPrincipal().hasRealm(assistanceAgent);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);

		super.getBuffer().addData(claim);

	}

	@Override
	public void bind(final Claim claim) {

		super.bindObject(claim, "passengerEmail", "description", "typeClaim", "leg");

	}

	@Override
	public void validate(final Claim claim) {
		boolean thereIsNotAnyTrackingLogs = true;
		Collection<TrackingLog> trackingLogs = this.repository.findAllTrackingLogsByClaimId(claim.getId());
		thereIsNotAnyTrackingLogs = trackingLogs.isEmpty();
		super.state(thereIsNotAnyTrackingLogs, "*", "acme.validation.claim.thereIsTrackingLogs.message");
		boolean confirmation;
		confirmation = super.getRequest().getData("confirmation", boolean.class);

		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		super.state(claim.getDraftMode(), "*", "acme.validation.claim.invalid-draftmode.message");
	}

	@Override
	public void perform(final Claim claim) {
		Collection<TrackingLog> trackingLogs;
		trackingLogs = this.repository.findAllTrackingLogsByClaimId(claim.getId());
		this.repository.deleteAll(trackingLogs);
		this.repository.delete(claim);

	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		Collection<Leg> legs;
		SelectChoices typeChoices;
		SelectChoices legChoices;
		int assistanceAgentId;
		AssistanceAgent assistanceAgent;

		typeChoices = SelectChoices.from(TypeClaim.class, claim.getTypeClaim());
		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assistanceAgent = this.repository.findAssistanceAgentById(assistanceAgentId);
		legs = this.repository.findAllPublishedLegs(assistanceAgent.getAirline().getId());
		if (!legs.contains(claim.getLeg()))
			claim.setLeg(null);
		legChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		dataset = super.unbindObject(claim, "passengerEmail", "description", "typeClaim", "draftMode", "leg");
		dataset.put("types", typeChoices);
		dataset.put("legs", legChoices);
		dataset.put("trackingLogStatus", claim.getIndicator());

		super.getResponse().addData(dataset);

	}
}
