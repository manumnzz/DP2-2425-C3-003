
package acme.features.assistenceagent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.entities.S4.TrackingLog;
import acme.realms.AssistanceAgent;

public class AssistanceAgentTrackingLogShowService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repo;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		TrackingLog tl;
		AssistanceAgent assistenceAgent;

		masterId = super.getRequest().getData("id", int.class);
		tl = this.repo.findTrackingLogById(masterId);
		assistenceAgent = tl == null ? null : tl.getAssistenceAgent();
		status = super.getRequest().getPrincipal().hasRealm(assistenceAgent) || tl != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog tl;
		int id;

		id = super.getRequest().getPrincipal().getActiveRealm().getId();
		tl = this.repo.findTrackingLogById(id);

		super.getBuffer().addData(tl);
	}

	@Override
	public void unbind(final TrackingLog object) {
		assert object != null;
		int masterId;
		boolean autorizado;
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();

		masterId = super.getRequest().getData("id", int.class);
		TrackingLog tl = this.repo.findTrackingLogById(masterId);
		autorizado = tl != null && tl.getAssistenceAgent().getUserAccount().getId() == userAccountId;

		Dataset dataset;

		dataset = super.unbindObject(object, "updateTime", "step", "resolutionPercentage", "indicator", "resolution, draftMode");
		dataset.put("autorizado", autorizado);

		super.getResponse().addData(dataset);
	}
}
