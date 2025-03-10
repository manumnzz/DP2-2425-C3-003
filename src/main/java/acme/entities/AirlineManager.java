
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import org.dom4j.tree.AbstractEntity;

import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AirlineManager extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@NotNull
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$", message = "Invalid identifier format")
	private String				identifier;

	@Mandatory
	@Positive
	private int					yearsOfExperience;

	@ValidMoment(past = true)
	@Mandatory
	@NotNull
	private Date				dateOfBirth;

	@Optional
	private String				pictureUrl;
}
