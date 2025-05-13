
package acme.features.customer.bookingPassenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.S2.Booking;
import acme.entities.S2.BookingPassenger;
import acme.entities.S2.Passenger;

@Repository
public interface CustomerBookingPassengerRepository extends AbstractRepository {

	@Query("select bp from BookingPassenger bp where bp.booking.customer.id = :customerId")
	Collection<BookingPassenger> findAllBookingPassengerByCustomerId(int customerId);

	@Query("select bp from BookingPassenger bp where bp.id =:bpId")
	BookingPassenger findBookingPassengerById(int bpId);

	@Query("select bk from Booking bk where bk.customer.id = :id")
	Collection<Booking> findBookingByCustomerId(int id);

	@Query("select bp.passenger from BookingPassenger bp where bp.booking.customer.id = :id")
	Collection<Passenger> findPassengerByCustomerId(int id);

	@Query("select bp.passenger from BookingPassenger bp where bp.booking.id = :bookingId")
	Collection<Passenger> findPassengersByBookingId(int bookingId);

	@Query("select count(bp) from BookingPassenger bp where bp.booking.id = :bookingId and bp.passenger.id = :passengerId")
	int existsBookingPassenger(int bookingId, int passengerId);

	@Query("select b from Booking b where b.id =:bookingId")
	Booking findBookingById(int bookingId);

	@Query("select count(bp) from BookingPassenger bp where bp.passenger.id = :passengerId and bp.booking.customer.id = :customerId")
	int existsBookingPassengerForCustomer(int passengerId, int customerId);

}
