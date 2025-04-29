
package acme.features.assistenceagent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S4.Claim;
import acme.entities.S4.Claim.Type;
import acme.entities.S4.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repo;

	// AbstractService<AirlineManager, Flight> -------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Claim claim;
		AssistanceAgent aa;

		masterId = super.getRequest().getData("id", int.class);
		claim = this.repo.findClaimById(masterId);
		aa = claim == null ? null : claim.getAssistenceAgent();
		status = claim != null && super.getRequest().getPrincipal().hasRealm(aa);

		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);

		Claim object = this.repo.findClaimById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Claim object) {
		assert object != null;

		super.bindObject(object, "moment", "passengerEmail", "description", "type", "indicator, draftMode");
	}

	@Override
	public void validate(final Claim object) {
		assert object != null;

	}

	@Override
	public void perform(final Claim object) {
		assert object != null;

		object.setDraftMode(false);
		Collection<TrackingLog> ls = this.repo.findTrackingLogByClaimId(object.getId());
		ls.stream().forEach(us -> us.setDraftMode(false));

		this.repo.save(object);
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
