
package acme.features.airlinemanager.flights;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Airport;
import acme.entities.S1.Flight;
import acme.entities.S1.FlightLeg;
import acme.entities.S1.Leg;
import acme.realms.AirlineManager;

@Repository
public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("select f from Flight f where f.airlineManager = :id")
	Collection<Flight> findFlightsByAirlineManagerId(int id);

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("select fl from FlightLeg fl where fl.flight.id = :id")
	Collection<FlightLeg> findLegsByFlightId(int id);

	@Query("SELECT l FROM Leg l WHERE l.id = :id")
	Leg findLegById(@Param("id") int id);

	@Query("SELECT l FROM Leg l WHERE l.id = :id")
	Leg findLegsById(@Param("id") int id);

	@Query("SELECT a FROM Airport a WHERE a.id = :id")
	Airport findAirportById(@Param("id") int id);

	@Query("SELECT am FROM AirlineManager am WHERE am.id = :id")
	AirlineManager findAirlineManagerById(@Param("id") int airlineManagerId);

	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirports();

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLegs();

}
