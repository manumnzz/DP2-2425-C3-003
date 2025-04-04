
package acme.features.airlinemanager.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Aircraft;
import acme.entities.Airport;
import acme.entities.S1.Flight;
import acme.entities.S1.FlightLeg;
import acme.entities.S1.Leg;
import acme.realms.AirlineManager;

@Repository
public interface AirlineManagerLegRepository extends AbstractRepository {

	@Query("SELECT l from Leg l where l.id = :id ")
	Leg findLegById(int id);

	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirports();

	@Query("SELECT a FROM Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("SELECT fl.leg FROM FlightLeg fl WHERE fl.flight.id = :flightId")
	Collection<Leg> findAllLegsByFlightId(@Param("flightId") int flightId);

	@Query("select a from Airport a where a.id = :departureAirportId")
	Airport findAirportById(int departureAirportId);

	@Query("select a from Aircraft a where a.id = :aircraftId")
	Aircraft findAircraftById(int aircraftId);

	@Query("select a from AirlineManager a where a.id = :airlineManagerId")
	AirlineManager findAirlineManagerById(int airlineManagerId);

	@Query("select fl from FlightLeg fl where fl.leg.id = :id")
	Collection<FlightLeg> findFlightsByLegId(int id);

	@Query("SELECT fl.flight FROM FlightLeg fl WHERE fl.leg.id = :legId")
	Flight findFlightByLegId(@Param("legId") int legId);

	@Query("SELECT MAX(fl.sequence) FROM FlightLeg fl WHERE fl.flight.id = :flightId")
	Integer findMaxSequenceByFlightId(@Param("flightId") int flightId);

}
