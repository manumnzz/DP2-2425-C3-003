
package acme.features.administrator.airport;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.Administrator;
import acme.client.repositories.AbstractRepository;
import acme.entities.Airport;
import acme.entities.S1.Flight;
import acme.entities.S1.FlightLeg;

@Repository
public interface AdministratorAirportRepository extends AbstractRepository {

	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirports();

	@Query("select a from Airport a where a.id = :masterId")
	Airport findAirportById(int masterId);

	@Query("select a from Administrator a where a.id = :administratorId")
	Administrator findAdministratorById(int administratorId);

	@Query("select f from Flight f where f.originAirport.id = :id or f.destinationAirport.id = :id")
	Collection<Flight> findFlightsByAirportId(int id);

	@Query("SELECT fl FROM FlightLeg fl WHERE fl.flight IN :flights")
	Collection<FlightLeg> findFlightLegsByFlights(Collection<Flight> flights);

}
