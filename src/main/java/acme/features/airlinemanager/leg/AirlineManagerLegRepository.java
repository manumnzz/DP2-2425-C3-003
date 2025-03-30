
package acme.features.airlinemanager.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Airport;
import acme.entities.S1.FlightLeg;
import acme.entities.S1.Leg;
import acme.entities.aircraft.Aircraft;
import acme.realms.AirlineManager;

@Repository
public interface AirlineManagerLegRepository extends AbstractRepository {

	@Query("SELECT l from Leg l where l.id = :id ")
	Leg findLegById(int id);

	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirports();

	@Query("SELECT a FROM Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("select l from Leg l where l.airlineManager.id = :id")
	Collection<Leg> findLegsByAirlineManagerId(@Param("id") int id);

	@Query("select a from Airport a where a.id = :departureAirportId")
	Airport findAirportById(int departureAirportId);

	@Query("select a from Aircraft a where a.id = :aircraftId")
	Aircraft findAircraftById(int aircraftId);

	@Query("select a from AirlineManager a where a.id = :airlineManagerId")
	AirlineManager findAirlineManagerById(int airlineManagerId);

	@Query("select fl from FlightLeg fl where fl.leg.id = :id")
	Collection<FlightLeg> findFlightsByLegId(int id);
}
