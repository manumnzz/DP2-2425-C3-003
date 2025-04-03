
package acme.features.administrator.airport;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Airport;
import acme.entities.OperationalScope;
import acme.entities.S1.Flight;
import acme.entities.S1.FlightLeg;

@GuiService
public class AdministratorAirportDeleteService extends AbstractGuiService<Administrator, Airport> {

	//Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirportRepository repository;

	//AbstractGuiService interface ---------------------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airport airport;

		airport = new Airport();

		super.getBuffer().addData(airport);
	}

	@Override
	public void bind(final Airport airport) {
		super.bindObject(airport, "name", "iataCode", "operationalScope", "city", "country", "website", "email", "phone");
	}

	@Override
	public void validate(final Airport airport) {
		;
	}

	@Override
	public void perform(final Airport airport) {
		Collection<Flight> flights;
		Collection<FlightLeg> flightLegs;

		flights = this.repository.findFlightsByAirportId(airport.getId());
		flightLegs = this.repository.findFlightLegsByFlights(flights);

		this.repository.deleteAll(flightLegs);
		this.repository.deleteAll(flights);
		this.repository.delete(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;
		SelectChoices choices;

		choices = SelectChoices.from(OperationalScope.class, airport.getOperationalScope());

		dataset = super.unbindObject(airport, "name", "iataCode", "city", "country", "website", "email", "phone");
		dataset.put("operationalScopes", choices);

		super.getResponse().addData(dataset);
	}

}
