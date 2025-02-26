
package acme.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "airline_managers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AirlineManager {

	@Id
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$", message = "Identifier must match pattern XX123456 or XXX123456")
	@Column(unique = true, nullable = false, length = 9)
	@NotNull(message = "Identifier cannot be null")
	@Size(min = 9, max = 9, message = "Identifier must be exactly 9 characters")
	private String		identifier;

	@Positive(message = "Years of experience must be positive")
	@Column(nullable = false)
	@NotNull(message = "Years of experience cannot be null")
	@Min(value = 1, message = "Years of experience must be at least 1")
	private int			yearsOfExperience;

	@Past(message = "Date of birth must be in the past")
	@Column(nullable = false)
	@NotNull(message = "Date of birth cannot be null")
	@Temporal(TemporalType.DATE)
	private LocalDate	dateOfBirth;

	@URL(message = "Picture URL must be a valid URL")
	@Size(max = 255, message = "Picture URL must not exceed 255 characters")
	private String		pictureUrl; // Optional
}
