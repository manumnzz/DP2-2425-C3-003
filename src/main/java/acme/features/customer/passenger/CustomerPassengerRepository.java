
package acme.features.customer.passenger;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.S2.Passenger;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("select p from Passenger p where p.id = :passengerId")
	Passenger findPassengerById(final int passengerId);

}
