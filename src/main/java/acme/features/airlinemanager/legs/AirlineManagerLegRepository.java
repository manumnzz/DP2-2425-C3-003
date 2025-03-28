
package acme.features.airlinemanager.legs;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Airport;
import acme.entities.S1.Leg;
import acme.entities.aircraft.Aircraft;

@Repository
public interface AirlineManagerLegRepository extends AbstractRepository {

	@Query("SELECT l from Leg l where l.id = :id ")
	Leg findLegById(int id);

	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirports();

	@Query("SELECT a FROM Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("SELECT l from Leg l where l.airlineManager = :id")
	Collection<Leg> findLegsByAirlineManagerId(int id);
}
