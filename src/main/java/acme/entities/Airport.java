
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.dom4j.tree.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airport extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				id;

	@Column(length = 50, nullable = false)
	@Size(max = 50)
	private String				name;

	@Column(length = 3, unique = true, nullable = false)
	@Pattern(regexp = "^[A-Z]{3}$", message = "IATA code must be exactly 3 uppercase letters")
	private String				iataCode;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OperationalScope	operationalScope;

	@Column(length = 50, nullable = false)
	@Size(max = 50)
	private String				city;

	@Column(length = 50, nullable = false)
	@Size(max = 50)
	private String				country;

	private String				website;

	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+$", message = "Invalid email format (no '@' allowed)")
	private String				email;

	@Pattern(regexp = "^\\+?\\d{6,15}$", message = "Phone number must be 6-15 digits, optional + prefix")
	private String				phoneNumber;
}

enum OperationalScope {
	INTERNATIONAL, DOMESTIC, REGIONAL
}
