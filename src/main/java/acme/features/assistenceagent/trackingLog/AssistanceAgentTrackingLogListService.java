
package acme.features.assistenceagent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S4.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

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
		Collection<TrackingLog> tl;
		int id;

		id = super.getRequest().getPrincipal().getActiveRealm().getId();
		tl = this.repo.findTrackingLogByAssisntenceAgentId(id);

		super.getBuffer().addData(tl);
	}

	@Override
	public void unbind(final TrackingLog object) {
		Dataset dataset;

		// Unbind de los atributos normales
		dataset = super.unbindObject(object, "updateTime", "step", "resolutionPercentage", "indicator", "resolution, draftMode");
		super.addPayload(dataset, object, "updateTime", "step", "resolutionPercentage", "indicator", "resolution, draftMode");

		// Añadir la respuesta con los datos al dataset
		super.getResponse().addData(dataset);
	}

}
