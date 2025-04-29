
package acme.features.assistenceagent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.services.GuiService;
import acme.entities.S4.Claim;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimListService extends AbstractService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repo;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Claim> claim;
		int id;

		id = super.getRequest().getPrincipal().getActiveRealm().getId();
		claim = this.repo.findClaimsByAssisntenceAgentId(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;

		// Unbind de los atributos normales
		dataset = super.unbindObject(claim, "moment", "passengerEmail", "description", "type", "indicator, draftMode");
		super.addPayload(dataset, claim, "moment", "passengerEmail", "description", "type", "indicator, draftMode");

		// Añadir la respuesta con los datos al dataset
		super.getResponse().addData(dataset);
	}
}
