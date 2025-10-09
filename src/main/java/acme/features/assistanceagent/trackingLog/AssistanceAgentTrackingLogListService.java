
package acme.features.assistanceagent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.S4.Claim;
import acme.entities.S4.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Claim claim;
		int userAccountId = super.getRequest().getPrincipal().getAccountId();
		AssistanceAgent assistanceAgent = this.repository.findAssistanceAgentIdByUserAccountId(userAccountId);
		
		
		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimByMasterId(masterId);
		AssistanceAgent owner = this.repository.findAssistanceAgentIdByClaimId(claim.getId());
		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class) && claim != null
			&& assistanceAgent.getId() == owner.getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TrackingLog> trackingLogs;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);

		trackingLogs = this.repository.findTrackingLogsByMasterId(masterId);
		super.getBuffer().addData(trackingLogs);
	}

	@Override
	public void unbind(final TrackingLog tr) {
		Dataset dataset;
		dataset = super.unbindObject(tr, "updateTime", "status", "resolutionPercentage");
		Claim claim;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimByMasterId(masterId);
		dataset.put("claim", claim);

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<TrackingLog> trackingLog) {
		int masterId;
		boolean claimCompleted = true;

		masterId = super.getRequest().getData("masterId", int.class);
		if (this.repository.findTrackingLogs100PercentageByMasterId(masterId).size() >= 2)
			claimCompleted = false;

		super.getResponse().addGlobal("claimCompleted", claimCompleted);
		super.getResponse().addGlobal("masterId", masterId);
	}

}