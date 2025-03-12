
package acme.entities.S4;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Claim extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@ValidMoment(past = true)
	@Mandatory
	@NotNull
	private Date				moment;

	@Mandatory
	@ValidEmail
	@Length(min = 0, max = 255)
	private String				passengerEmail;

	@ValidString(min = 0, max = 255)
	@Mandatory
	private String				description;

	@Mandatory
	private Type				type;

	@Mandatory
	private boolean				indicator;


	public enum Type {
		FLIGHT_ISSUES, LUGGAGE_ISSUES, SECURITY_INCIDENT, OTHER
	}
}
