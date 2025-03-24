
package acme.features.airlinemanager.flights;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.S1.Flight;
import acme.entities.S1.FlightLeg;

@Repository
public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("select f from Flight f where f.airlineManager = :id")
	Collection<Flight> findFlightsByAirlineManagerId(int id);

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("select fl from FlightLeg fl where fl.flight.id = :id")
	Collection<FlightLeg> findLegsByFlightId(int id);

}
