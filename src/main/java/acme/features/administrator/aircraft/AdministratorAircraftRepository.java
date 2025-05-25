
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.Administrator;
import acme.client.repositories.AbstractRepository;
import acme.entities.Aircraft;
import acme.entities.Airline;

@Repository
public interface AdministratorAircraftRepository extends AbstractRepository {

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("select a from Aircraft a where a.id = :masterId")
	Aircraft findAircraftById(int masterId);

	@Query("select a from Administrator a where a.id = :administratorId")
	Administrator findAdministratorById(int administratorId);

	@Query("select a from Airline a")
	Collection<Airline> findAllAirlines();

	@Query("select a from Airline a where a.name = :airlineName")
	Airline findAirlineByName(String airlineName);

}
