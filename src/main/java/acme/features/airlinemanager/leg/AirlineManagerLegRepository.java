
package acme.features.airlinemanager.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Aircraft;
import acme.entities.Airport;
import acme.entities.S1.Flight;
import acme.entities.S1.Leg;

@Repository
public interface AirlineManagerLegRepository extends AbstractRepository {

	@Query("SELECT l FROM Leg l WHERE l.id = :legId")
	Leg findLegById(int legId);

	@Query("SELECT f FROM Flight f WHERE f.id = :flightId")
	Flight findFlightById(int flightId);

	@Query("SELECT a FROM Aircraft a WHERE a.id = :aircraftId")
	Aircraft findAircraftById(int aircraftId);

	@Query("SELECT a FROM Airport a WHERE a.id = :airportId")
	Airport findAirportById(int airportId);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledDeparture ASC")
	Collection<Leg> findLegsByFlightId(int flightId);

	@Query("SELECT a FROM Aircraft a WHERE a.airline.id = :airlineId")
	Collection<Aircraft> findAircraftsByAirline(int airlineId);

	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirports();

	@Query("SELECT a FROM Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("SELECT l FROM Leg l WHERE l.flightNumber = :flightNumber AND l.id <> :id")
	Collection<Leg> findByFlightNumber(@Param("flightNumber") String flightNumber, @Param("id") int id);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId AND l.id <> :id")
	Collection<Leg> findByFlightId(@Param("flightId") int flightId, @Param("id") int id);

	@Query("SELECT l FROM Leg l WHERE l.aircraft.id = :aircraftId AND l.id <> :id")
	Collection<Leg> findByAircraftId(@Param("aircraftId") int aircraftId, @Param("id") int id);

}
