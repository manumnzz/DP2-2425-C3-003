
package acme.features.assistanceagent.trackingLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S4.Claim;
import acme.entities.S4.TrackingLog;
import acme.entities.S4.TrackingLogStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean res;
		boolean isAssistanceAgent;
		String metodo = super.getRequest().getMethod();
		boolean correctEnum = true;
		boolean claimAlreadyCompleted;
		String status;
		boolean isClaimCreator = false;
		int userAccountId;
		int assistanceAgentId;
		int ownerId;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		userAccountId = super.getRequest().getPrincipal().getAccountId();
		assistanceAgentId = this.repository.findAssistanceAgentIdByUserAccountId(userAccountId).getId();
		Claim claim;
		AssistanceAgent assistanceAgent;
		assistanceAgent = this.repository.findAssistanceAgentIdByClaimId(masterId);

		isAssistanceAgent = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		if (assistanceAgent != null) {
			ownerId = assistanceAgent.getId();
			isClaimCreator = assistanceAgentId == ownerId;
		}

		claimAlreadyCompleted = true;
		List<TrackingLog> list100TrackingLogs = new ArrayList<>(this.repository.findTrackingLogs100PercentageByMasterId(masterId));
		if (list100TrackingLogs.size() >= 2)
			claimAlreadyCompleted = false;

		boolean fakeUpdate = true;

		if (super.getRequest().hasData("id")) {
			Integer id = super.getRequest().getData("id", Integer.class);
			if (id != 0)
				fakeUpdate = false;
		}

		claim = this.repository.findClaimByMasterId(masterId);
		res = isAssistanceAgent && isClaimCreator && claim != null && fakeUpdate && claimAlreadyCompleted;

		if (metodo.equals("POST")) {
			status = super.getRequest().getData("status", String.class);
			if (!Arrays.toString(TrackingLogStatus.values()).concat("0").contains(status))
				correctEnum = false;
			res = correctEnum && fakeUpdate && claimAlreadyCompleted;
		}

		super.getResponse().setAuthorised(res);

	}

	@Override
	public void load() {
		TrackingLog tr;

		tr = new TrackingLog();
		super.getBuffer().addData(tr);
	}

	@Override
	public void bind(final TrackingLog tr) {
		Claim claim;
		Date moment = MomentHelper.getCurrentMoment();
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);

		claim = this.repository.findClaimByMasterId(masterId);
		super.bindObject(tr, "step", "resolutionPercentage", "status", "resolution");
		tr.setUpdateTime(moment);
		tr.setDraftMode(true);
		tr.setClaim(claim);
	}

	@Override
	public void perform(final TrackingLog tr) {
		this.repository.save(tr);
	}

	@Override
	public void validate(final TrackingLog tr) {
		boolean confirmation;
		Claim claim;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		double trMaxPercentage = 0.0;
		List<TrackingLog> trackingLogs100percentage;
		List<TrackingLog> trackingLogMaxList;
		TrackingLog trMaxResolutionPercentage;
		claim = this.repository.findClaimByMasterId(masterId);
		trackingLogs100percentage = new ArrayList<>(this.repository.findTrackingLogs100PercentageByMasterId(claim.getId()));
		boolean repetido = false;

		trackingLogMaxList = new ArrayList<>(this.repository.findTopByClaimIdOrderByResolutionPercentageDesc(tr.getClaim().getId()));
		if (!trackingLogMaxList.isEmpty() && tr.getResolutionPercentage() != null) {
			trMaxResolutionPercentage = trackingLogMaxList.get(0);
			trMaxPercentage = trMaxResolutionPercentage.getResolutionPercentage();

			if (tr.getResolutionPercentage() <= trMaxPercentage && trackingLogs100percentage.isEmpty())
				super.state(false, "resolutionPercentage", "acme.validation.trackinglog.invalid-resolutionpercentage.message");

			if (trackingLogs100percentage.size() >= 2)
				super.state(false, "resolutionPercentage", "acme.validation.trackinglog.invalid-resolutionpercentage-two100.message");

			if (!trackingLogs100percentage.isEmpty() && tr.getResolutionPercentage() < 100)
				super.state(false, "resolutionPercentage", "acme.validation.trackinglog.invalid-resolution-percentage2.message");
			if (!trackingLogs100percentage.isEmpty() && trackingLogs100percentage.get(0).getStatus() != tr.getStatus()) {
				super.state(false, "status", "acme.validation.trackinglog.invalid-resolution-percentage3.message");
				repetido = true;
			}

			if (!trackingLogs100percentage.isEmpty() && trMaxResolutionPercentage.getDraftMode())
				super.state(false, "*", "acme.validation.trackinglog.invalid-allpublished2.message");

			if (tr.getStatus() != trMaxResolutionPercentage.getStatus() && !trackingLogs100percentage.isEmpty() && tr.getResolutionPercentage() < 100 && !repetido)
				super.state(false, "status", "acme.validation.trackinglog.invalid-resolution-percentage3.message");
			
			if(tr.getResolutionPercentage() == 100 && tr.getResolution().isBlank()) {
				super.state(false, "resolution", "acme.validation.trackinglog.invalid-resolution.message");
			}
			if(tr.getResolutionPercentage() == 100 && tr.getStatus() == TrackingLogStatus.PENDING && tr.getStatus() != null) {
				super.state(false, "status", "acme.validation.trackinglog.invalid-status.message");
			}
			if(tr.getResolutionPercentage() < 100 && tr.getStatus() != TrackingLogStatus.PENDING && tr.getStatus() != null) {
				super.state(false, "status", "acme.validation.trackinglog.invalid-status-notresolute.message");
			}
		}

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		int masterId;
		Claim claim;
		SelectChoices choicesStatus;

		choicesStatus = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());
		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimByMasterId(masterId);
		dataset = super.unbindObject(trackingLog, "step", "resolutionPercentage", "status", "resolution");
		dataset.put("claim", claim);
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("draftMode", true);
		dataset.put("statuses", choicesStatus);
		super.getResponse().addData(dataset);

	}
}