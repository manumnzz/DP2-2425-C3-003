
package acme.features.assistenceagent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.entities.S4.TrackingLog;
import acme.realms.AssistanceAgent;

public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repo;


	@Override
	public void authorise() {
		final Principal principal = super.getRequest().getPrincipal();

		final boolean status = principal.hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog tl;
		AssistanceAgent assistenceAgent;

		assistenceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		tl = new TrackingLog();
		tl.setAssistenceAgent(assistenceAgent);

		super.getBuffer().addData(tl);
	}

	@Override
	public void bind(final TrackingLog object) {
		assert object != null;

		super.bindObject(object, "updateTime", "step", "resolutionPercentage", "indicator", "resolution, draftMode");

	}

	@Override
	public void validate(final TrackingLog object) {
		assert object != null;
	}

	@Override
	public void perform(final TrackingLog object) {
		assert object != null;
		this.repo.save(object);
	}

	@Override
	public void unbind(final TrackingLog object) {
		assert object != null;

		Dataset dataset = super.unbindObject(object, "updateTime", "step", "resolutionPercentage", "indicator", "resolution, draftMode");

		super.getResponse().addData(dataset);
	}
}
