
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.S4.TrackingLog;
import acme.entities.S4.TrackingLogStatus;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (trackingLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (trackingLog.getResolutionPercentage() != null && trackingLog.getStatus() != null)
			if (trackingLog.getStatus().equals(TrackingLogStatus.PENDING) && trackingLog.getResolutionPercentage() == 100.00)
				super.state(context, false, "status", "acme.validation.trackinglog.invalid-indicator.message");
			else if (!trackingLog.getStatus().equals(TrackingLogStatus.PENDING) && trackingLog.getResolutionPercentage() != 100.00)
				super.state(context, false, "status", "acme.validation.trackinglog.invalid-indicator.message");
			else if (!trackingLog.getStatus().equals(TrackingLogStatus.PENDING) && (trackingLog.getResolution() == null || trackingLog.getResolution().isBlank()))
				super.state(context, false, "status", "acme.validation.trackinglog.invalid-resolution.message");
		result = !super.hasErrors(context);
		return result;
	}

}
