
package acme.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SystemConfiguration extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}$", message = "Must be a valid 3-letter currency code")
	@Automapped
	private String				systemCurency;

	//@Mandatory
	//@ValidString(pattern = "^[A-Z]{3}$", message = "Must be a valid 3-letter currency code")
	//@Automapped
	//private List<String>		acceptedCurrencies;

	@Mandatory
	@ValidString
	@Automapped
	private String				acceptedCurrencies;


	public SystemConfiguration() {
		this.systemCurency = "EUR";
		List<String> initialCurrencies = new ArrayList<>();
		initialCurrencies.add("EUR");
		initialCurrencies.add("USD");
		initialCurrencies.add("GBP");
	}

	// Methods to manage accepted currencies
	//	public void addAcceptedCurrency(final String currency) {
	//		if (!this.acceptedCurrencies.contains(currency))
	//			this.acceptedCurrencies.add(currency);
	//	}
	//
	//	public void removeAcceptedCurrency(final String currency) {
	//		this.acceptedCurrencies.remove(currency);
	//	}

}
