
package acme.features.assistanceagent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S1.Leg;
import acme.entities.S4.Claim;
import acme.entities.S4.TypeClaim;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimUpdateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		Claim claim;
		int claimId;
		int userAccountId;
		int assistanceAgentId;
		int ownerId;
		boolean res = true;
		boolean isClaimCreator = false;
		boolean isAssistanceAgent;
		int legId;
		Leg leg;
		Collection<Leg> publishedLegs;
		String type;
		String metodo = super.getRequest().getMethod();
		boolean correctEnum = false;
		boolean correctLeg = true;
		AssistanceAgent assistanceAgent;
		int agentId;
		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assistanceAgent = this.repository.findAssistanceAgentById(agentId);

		if (!super.getRequest().hasData("id"))
			res = false;
		else {
			claimId = super.getRequest().getData("id", int.class);
			claim = this.repository.findClaimById(claimId);

			isAssistanceAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
			claimId = super.getRequest().getData("id", int.class);
			userAccountId = super.getRequest().getPrincipal().getAccountId();
			assistanceAgentId = this.repository.findAssistanceAgentIdByUserAccountId(userAccountId);

			claim = this.repository.findClaimById(claimId);
			if (claim != null) {
				ownerId = this.repository.findAssistanceAgentIdByClaimId(claimId);
				isClaimCreator = assistanceAgentId == ownerId;
			}

			res = claim != null && isAssistanceAgent && isClaimCreator && claim.getDraftMode();
			if (metodo.equals("POST")) {
				type = super.getRequest().getData("typeClaim", String.class);
				legId = super.getRequest().getData("leg", int.class);
				leg = this.repository.findLegById(legId);
				publishedLegs = this.repository.findAllPublishedLegs(assistanceAgent.getAirline().getId());
				for (TypeClaim t : TypeClaim.values())
					if (t.name().equals(type))
						correctEnum = true;
				if (!publishedLegs.contains(leg))
					correctLeg = false;
				res = false;
				if (claim != null)
					res = correctEnum && correctLeg && claim.getDraftMode();
			}
		}
		super.getResponse().setAuthorised(res);
	}

	@Override
	public void load() {
		int id;
		Claim claim;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int userAccountId;
		AssistanceAgent assistanceAgent;
		Integer legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		assistanceAgent = this.repository.findAssistanceAgentByUserAccountId(userAccountId);
		super.bindObject(claim, "passengerEmail", "description", "typeClaim");

		claim.setAssistanceAgent(assistanceAgent);
		claim.setLeg(leg);

	}

	@Override
	public void perform(final Claim claim) {
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
		SelectChoices selectedLeg;
		Collection<Leg> legs;
		SelectChoices choicesType;
		AssistanceAgent assistanceAgent;
		int agentId;
		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assistanceAgent = this.repository.findAssistanceAgentById(agentId);

		choicesType = SelectChoices.from(TypeClaim.class, claim.getTypeClaim());
		legs = this.repository.findAllPublishedLegs(assistanceAgent.getAirline().getId());
		selectedLeg = SelectChoices.from(legs, "flightNumber", claim.getLeg());
		dataset = super.unbindObject(claim, "moment", "passengerEmail", "description", "typeClaim");
		dataset.put("draftMode", claim.getDraftMode());
		dataset.put("legs", selectedLeg);
		dataset.put("leg", selectedLeg.getSelected().getKey());
		dataset.put("typeClaim", choicesType);

		super.getResponse().addData(dataset);
	}

}
