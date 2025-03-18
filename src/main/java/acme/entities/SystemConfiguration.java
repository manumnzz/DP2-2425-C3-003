
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
	private String				systemCurrency;

	@Mandatory
	@ValidString
	@Automapped
	private String				acceptedCurrencies;


	public SystemConfiguration() {
		this.systemCurrency = "EUR";
		List<String> initialCurrencies = new ArrayList<>();
		initialCurrencies.add("EUR");
		initialCurrencies.add("USD");
		initialCurrencies.add("GBP");
	}

	// Getter y Setter para acceptedCurrencies como List<String>
	public List<String> getAcceptedCurrencies() {
		if (this.acceptedCurrencies == null || this.acceptedCurrencies.isEmpty())
			return new ArrayList<>();
		return List.of(this.acceptedCurrencies.split(","));
	}

	public void setAcceptedCurrencies(final List<String> currencies) {
		this.acceptedCurrencies = String.join(",", currencies);
	}

	// Methods to manage accepted currencies
	public void addAcceptedCurrency(final String currency) {
		List<String> currencies = new ArrayList<>(this.getAcceptedCurrencies());
		if (!currencies.contains(currency)) {
			currencies.add(currency);
			this.setAcceptedCurrencies(currencies);
		}
	}

	public void removeAcceptedCurrency(final String currency) {
		List<String> currencies = new ArrayList<>(this.getAcceptedCurrencies());
		currencies.remove(currency);
		this.setAcceptedCurrencies(currencies);
	}

}
