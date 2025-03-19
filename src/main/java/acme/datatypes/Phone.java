
package acme.datatypes;

import javax.persistence.Embeddable;

import acme.client.components.basis.AbstractDatatype;
import acme.constraints.ValidPhone;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@ValidPhone
public class Phone extends AbstractDatatype {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private String				number;

	// Object interface -------------------------------------------------------


	@Override
	public String toString() {
		StringBuilder result;

		result = new StringBuilder();
		result.append("<<+");
		result.append(this.number);
		result.append(">>");

		return result.toString();
	}
}
