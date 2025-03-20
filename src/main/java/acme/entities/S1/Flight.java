
package acme.entities.S1;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import acme.entities.Airport;
import acme.realms.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidShortText
	@Automapped
	private String				tag;

	@Mandatory
	@Valid
	@Automapped
	private boolean				requiresSelfTransfer;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidLongText
	@Automapped
	private String				description;

	// Derived attributes -----------------------------------------------------


	public Date getScheduledDeparture() {
		return this.firstLeg != null ? this.firstLeg.getScheduledDeparture() : null;
	}

	public Date getScheduledArrival() {
		return this.lastLeg != null ? this.lastLeg.getScheduledArrival() : null;
	}

	public String getOriginCity() {
		return this.originAirport != null ? this.originAirport.getCity() : null;
	}

	public String getDestinationCity() {
		return this.destinationAirport != null ? this.destinationAirport.getCity() : null;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = true)
	private Leg				firstLeg;

	@Mandatory
	@Valid
	@ManyToOne(optional = true)
	private Leg				lastLeg;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport			originAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport			destinationAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AirlineManager	airlineManager;

}
