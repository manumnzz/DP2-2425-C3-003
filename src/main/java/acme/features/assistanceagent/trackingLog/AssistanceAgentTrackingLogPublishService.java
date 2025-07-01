
package acme.features.assistanceagent.trackingLog;

import java.util.Collection;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S4.Claim;
import acme.entities.S4.TrackingLog;
import acme.entities.S4.TrackingLogStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogPublishService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int trackingLogId;
		TrackingLog trackingLog;
		Claim claim;
		AssistanceAgent assistanceAgent;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);
		claim = trackingLog == null ? null : trackingLog.getClaim();
		assistanceAgent = claim == null ? null : claim.getAssistanceAgent();
		status = claim != null && trackingLog != null && !claim.getDraftMode() && trackingLog.getDraftMode() && super.getRequest().getPrincipal().hasRealm(assistanceAgent);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackinglog) {

		super.bindObject(trackinglog, "step", "resolutionPercentage", "status", "resolution");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		boolean isNotWrongResolutionPercentage = true;
		boolean isNotWrongResolutionPercentage2 = true;
		boolean isNotWrongResolution = true;
		boolean isNotWrongResolution2 = true;
		boolean isNotMaxCompleted = true;
		boolean isWrongResolutionPercentage4 = true;

		if (trackingLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() < 100.0 && trackingLog.getStatus() != null)
			isNotWrongResolutionPercentage = trackingLog.getStatus().equals(TrackingLogStatus.PENDING);
		else if (trackingLog.getStatus() != null)
			isNotWrongResolutionPercentage2 = !trackingLog.getStatus().equals(TrackingLogStatus.PENDING);

		if (trackingLog.getStatus() != null && trackingLog.getStatus().equals(TrackingLogStatus.PENDING))
			isNotWrongResolution = trackingLog.getResolution() == null || trackingLog.getResolution().isBlank();
		else
			isNotWrongResolution2 = trackingLog.getResolution() != null && !trackingLog.getResolution().isBlank();

		if (trackingLog.getClaim() != null) {
			TrackingLog highestTrackingLog;
			Collection<TrackingLog> trackingLogs = this.repository.findOrderTrackingLogs(trackingLog.getClaim().getId());
			if (trackingLog.getResolutionPercentage() != null && trackingLogs.size() > 0) {
				highestTrackingLog = trackingLogs.stream().max(Comparator.comparingDouble(TrackingLog::getResolutionPercentage)).get();
				long completedTrackingLogs = trackingLogs.stream().filter(t -> t.getResolutionPercentage().equals(100.00)).count();
				if (highestTrackingLog.getId() != trackingLog.getId()) {
					if (highestTrackingLog.getResolutionPercentage() < trackingLog.getResolutionPercentage())
						isWrongResolutionPercentage4 = false;
					if (highestTrackingLog.getResolutionPercentage() == 100 && trackingLog.getResolutionPercentage() == 100)
						isNotMaxCompleted = !highestTrackingLog.getDraftMode() && completedTrackingLogs < 2;
				}
			}

		}

		super.state(!trackingLog.getClaim().getDraftMode(), "draftMode", "acme.validation.trackingLog.claimDraftMode.message");
		super.state(isNotWrongResolutionPercentage, "resolutionPercentage", "acme.validation.trackingLog.resolutionPercentage.message");
		super.state(isNotWrongResolutionPercentage2, "resolutionPercentage", "acme.validation.trackingLog.resolutionPercentage2.message");
		super.state(isNotWrongResolution, "resolution", "acme.validation.trackingLog.isNotWrongResolution.message");
		super.state(isNotWrongResolution2, "resolution", "acme.validation.trackingLog.isNotWrongResolution2.message");
		super.state(isNotMaxCompleted, "resolutionPercentage", "acme.validation.trackingLog.isNotMaxCompleted.message");
		super.state(isWrongResolutionPercentage4, "resolutionPercentage", "acme.validation.trackingLog.isWrongResolutionPercentage3.message");

	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		trackingLog.setDraftMode(false);
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		SelectChoices StatusChoices;

		StatusChoices = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());
		dataset = super.unbindObject(trackingLog, "updateTime", "step", "resolutionPercentage", "status", "resolution", "draftMode");
		dataset.put("status", StatusChoices);
		dataset.put("masterId", trackingLog.getClaim().getId());

		super.getResponse().addData(dataset);

	}
}
