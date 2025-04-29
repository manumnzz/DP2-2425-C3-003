
package acme.features.assistenceagent.claim;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.client.services.GuiService;
import acme.entities.S4.Claim;
import acme.entities.S4.Claim.Type;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimShowService extends AbstractService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repo;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Claim claim;
		AssistanceAgent assistenceAgent;

		masterId = super.getRequest().getData("id", int.class);
		claim = this.repo.findClaimById(masterId);
		assistenceAgent = claim == null ? null : claim.getAssistenceAgent();
		status = super.getRequest().getPrincipal().hasRealm(assistenceAgent) || claim != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getPrincipal().getActiveRealm().getId();
		claim = this.repo.findClaimById(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim object) {
		assert object != null;
		int masterId;
		boolean autorizado;
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		SelectChoices choices = SelectChoices.from(Type.class, object.getType());

		masterId = super.getRequest().getData("id", int.class);
		Claim c = this.repo.findClaimById(masterId);
		autorizado = c != null && c.getAssistenceAgent().getUserAccount().getId() == userAccountId;

		Dataset dataset;

		dataset = super.unbindObject(object, "moment", "passengerEmail", "description", "type", "indicator, draftMode");
		dataset.put("type", choices);
		dataset.put("autorizado", autorizado);

		super.getResponse().addData(dataset);
	}

}
