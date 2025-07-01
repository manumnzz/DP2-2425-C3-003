
package acme.features.assistanceagent.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S1.Leg;
import acme.entities.S4.Claim;
import acme.entities.S4.TypeClaim;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		super.getResponse().setAuthorised(status);

		if (status) {
			String method;
			int claimId, legId, assistanceAgentId;
			Claim claim;
			Leg leg;
			AssistanceAgent assistanceAgent;
			Collection<Leg> legs;

			method = super.getRequest().getMethod();

			if (method.equals("GET"))
				status = true;
			else {
				assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
				assistanceAgent = this.repository.findAssistanceAgentById(assistanceAgentId);
				claimId = super.getRequest().getData("id", int.class);
				claim = this.repository.findClaimById(claimId);
				legId = super.getRequest().getData("leg", int.class);
				legs = this.repository.findAllPublishedLegs(assistanceAgent.getAirline().getId());
				leg = this.repository.findLegById(legId);
				status = (legId == 0 || legs.contains(leg)) && super.getRequest().getPrincipal().hasRealm(assistanceAgent);
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		AssistanceAgent assistanceAgent;
		Date registrationMoment;

		assistanceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		registrationMoment = MomentHelper.getCurrentMoment();

		claim = new Claim();

		claim.setMoment(registrationMoment);
		claim.setPassengerEmail("");
		claim.setDescription("");
		claim.setTypeClaim(TypeClaim.FLIGHT_ISSUES);
		claim.setAssistanceAgent(assistanceAgent);
		claim.setDraftMode(true);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {

		super.bindObject(claim, "passengerEmail", "description", "typeClaim", "leg");

	}

	@Override
	public void perform(final Claim claim) {
		Date registrationMoment;

		registrationMoment = MomentHelper.getCurrentMoment();
		claim.setMoment(registrationMoment);
		claim.setDraftMode(true);
		this.repository.save(claim);
	}

	@Override
	public void validate(final Claim claim) {
		boolean confirmation;
		confirmation = super.getRequest().getData("confirmation", boolean.class);

		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		super.state(claim.getDraftMode(), "*", "acme.validation.claim.invalid-draftmode.message");
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

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
