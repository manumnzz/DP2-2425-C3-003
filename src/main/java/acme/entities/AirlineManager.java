
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import org.checkerframework.common.aliasing.qual.Unique;
import org.dom4j.tree.AbstractEntity;

import acme.client.components.mappings.Automapped;
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
	@Unique
	@Column(unique = true, nullable = false)
	private String				identifier;

	@Mandatory
	@Positive
	@Automapped
	@Column(nullable = false)
	private int					yearsOfExperience;

	@ValidMoment(past = true)
	@Mandatory
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date				dateOfBirth;

	@Optional
	@Automapped
	private String				pictureUrl;
}
