
package acme.entities.S1;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidLongText;
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
	@ValidString(max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private Boolean				requiresSelfTransfer;

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
		return this.flightLeg != null && !this.flightLeg.isEmpty() ? this.flightLeg.stream().min(Comparator.comparingInt(FlightLeg::getSequence)).map(x -> x.getLeg().getScheduledDeparture()).orElse(null) : null;
	}

	public Date getScheduledArrival() {
		return this.flightLeg != null && !this.flightLeg.isEmpty() ? this.flightLeg.stream().max(Comparator.comparingInt(FlightLeg::getSequence)).map(x -> x.getLeg().getScheduledArrival()).orElse(null) : null;
	}

	public String getOriginCity() {
		return this.flightLeg != null && !this.flightLeg.isEmpty() ? this.flightLeg.stream().min(Comparator.comparingInt(FlightLeg::getSequence)).map(x -> x.getLeg().getDepartureAirport().getCity()).orElse(null) : null;
	}

	public String getDestinationCity() {
		return this.flightLeg != null && !this.flightLeg.isEmpty() ? this.flightLeg.stream().max(Comparator.comparingInt(FlightLeg::getSequence)).map(x -> x.getLeg().getArrivalAirport().getCity()).orElse(null) : null;
	}

	public Integer getNumberOfLayovers() {
		return this.flightLeg != null ? Math.max(0, this.flightLeg.size() - 1) : 0;
	}

	public Leg getFirstLeg() {
		return this.flightLeg.stream().min(Comparator.comparingInt(FlightLeg::getSequence)).map(FlightLeg::getLeg).orElse(null);
	}

	public Leg getLastLeg() {
		return this.flightLeg.stream().max(Comparator.comparing(FlightLeg::getSequence)).map(FlightLeg::getLeg).orElse(null);
	}
	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToMany
	private List<FlightLeg>	flightLeg;

	@Mandatory
	@Valid
	@ManyToOne
	private Airport			airport;

	@Mandatory
	@Valid
	@ManyToOne
	private AirlineManager	airlineManager;
}
