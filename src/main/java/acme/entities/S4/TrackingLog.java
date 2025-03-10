
package acme.entities.S4;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.constraints.Past;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrackingLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Past
	@Mandatory
	private Date				updateTime;

	@Mandatory
	@ValidString(min = 0, max = 50)
	private String				step;

	@Mandatory
	@ValidNumber(min = 0, max = 100, fraction = 0)
	private Integer				resolutionPercentage;

	private boolean				indicator;

	@Optional
	@ValidString(min = 0, max = 255)
	private String				resolution;
}
