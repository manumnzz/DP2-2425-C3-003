
package acme.entities.S1;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightLeg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidNumber()
	private Integer				sequence;

	// Derived attributes -----------------------------------------------------


	public Leg getFirstLeg() {
		return this.leg;
	}

	public Leg getLastLeg() {
		return this.leg;
	}

	public Integer getNumberOfLayovers() {
		return Math.max(0, this.sequence - 1);
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne
	private Flight	flight;

	@Mandatory
	@Valid
	@ManyToOne
	private Leg		leg;

}
