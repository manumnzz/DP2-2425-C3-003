
package acme.entities.S2;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.S1.Flight;
import acme.features.customer.passenger.CustomerPassengerRepository;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

@Table(indexes = {

	@Index(columnList = "locatorCode"), @Index(columnList = "draftMode")
})

public class Booking extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,8}$")
	@Column(unique = true)
	private String				locatorCode;

	@Optional
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	@Valid
	@Automapped
	private ClassType			travelClass;

	@Optional
	@ValidString(pattern = "^\\d{4}$")
	@Automapped
	private String				lastCreditCardNibble;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Money getPrice() {
		Money price = new Money();
		CustomerPassengerRepository repo = SpringHelper.getBean(CustomerPassengerRepository.class);

		if (this.getFlight() == null) {
			price.setAmount(0.0);
			price.setCurrency("EUR");
		} else {
			Flight flight = this.getFlight();

			long publishedPassengers = repo.findPassengersByBookingId(this.getId()).stream().filter(p -> !p.getDraftMode()).count();

			price.setAmount(flight.getCost().getAmount() * publishedPassengers);
			price.setCurrency(flight.getCost().getCurrency());
		}

		return price;
	}

	@Transient
	public Integer getNumberOfPassengers() {
		CustomerPassengerRepository repo = SpringHelper.getBean(CustomerPassengerRepository.class);
		return (int) repo.findPassengersByBookingId(this.getId()).stream().filter(p -> !p.getDraftMode()).count();
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer	customer;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight		flight;
}
