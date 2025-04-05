
package acme.features.airlinemanager.flight;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.S1.Flight;
import acme.entities.S1.Leg;

@Repository
public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("SELECT f FROM Flight f WHERE f.airlineManager.id = :airlineManagerId")
	Collection<Flight> findFlightsByManagerId(int airlineManagerId);

	@Query("SELECT f FROM Flight f WHERE f.id = :masterId")
	Flight findFlightById(int masterId);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId")
	Collection<Leg> findLegsByFlightId(int flightId);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId AND l.draftMode = false")
	Collection<Leg> findPublishedLegsByFlightId(int flightId);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledDeparture ASC")
	List<Leg> findByFlightIdOrdered(int flightId);

}
