
package acme.features.authenticated.airlinemanager;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.S1.Flight;

@Repository
public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("select a from AirlineManager a where a.identifier = :id")
	Flight findFlightByAirlineManagerId(int id);
}
