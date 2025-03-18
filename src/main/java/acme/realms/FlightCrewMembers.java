
package acme.realms;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.Airline;
import acme.entities.S1.Flight;
import acme.entities.S3.AvailabilityStatus;
import acme.entities.aircraft.Aircraft;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightCrewMembers extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	@Column(unique = true)
	private String				employeeCode;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Column(unique = true)
	private String				phone;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private List<String>		language;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private AvailabilityStatus	availability;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				salary;

	@Optional
	@ValidNumber(min = 0)
	@Automapped
	private Integer				experience;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne
	private Aircraft			aircraft;

	@Mandatory
	@Valid
	@ManyToOne
	private Flight				flight;

	@Mandatory
	@Valid
	@ManyToOne
	private Airline				airline;

}
