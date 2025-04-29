
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
public class AssistanceAgentClaimCreateService extends AbstractService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repo;


	@Override
	public void authorise() {
		final Principal principal = super.getRequest().getPrincipal();

		final boolean status = principal.hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		AssistanceAgent assistenceAgent;

		assistenceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		claim = new Claim();
		claim.setAssistenceAgent(assistenceAgent);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim object) {
		assert object != null;

		super.bindObject(object, "moment", "passengerEmail", "description", "type", "indicator, draftMode");
	}

	@Override
	public void validate(final Claim claim) {
		assert claim != null;
	}

	@Override
	public void perform(final Claim claim) {
		assert claim != null;
		this.repo.save(claim);
	}

	@Override
	public void unbind(final Claim object) {
		assert object != null;

		SelectChoices choices = SelectChoices.from(Type.class, object.getType());

		Dataset dataset = super.unbindObject(object, "moment", "passengerEmail", "description", "type", "indicator, draftMode");

		dataset.put("type", choices);

		super.getResponse().addData(dataset);
	}
}
