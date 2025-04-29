
package acme.features.assistenceagent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S4.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogDeleteService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repo;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		TrackingLog tl;
		AssistanceAgent aa;

		masterId = super.getRequest().getData("id", int.class);
		tl = this.repo.findTrackingLogById(masterId);
		aa = tl == null ? null : tl.getAssistenceAgent();
		status = tl != null && super.getRequest().getPrincipal().hasRealm(aa);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);

		TrackingLog object = this.repo.findTrackingLogById(id);

		super.getBuffer().addData(object);
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

		this.repo.delete(object);
	}

	@Override
	public void unbind(final TrackingLog object) {
		assert object != null;

		Dataset dataset = super.unbindObject(object, "updateTime", "step", "resolutionPercentage", "indicator", "resolution, draftMode");

		super.getResponse().addData(dataset);
	}
}
