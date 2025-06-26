
package acme.features.customer.passenger;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.S2.Booking;
import acme.entities.S2.BookingRecord;
import acme.entities.S2.Passenger;
import acme.realms.Customer;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("select p from Passenger p where p.id = :passengerId")
	Passenger findPassengerById(final int passengerId);

	@Query("select p from Passenger p where p.passportNumber = :passportNumber")
	Passenger findPassengerByPassportNumber(String passportNumber);

	@Query("select bp from BookingRecord bp where bp.booking.customer.id = :customerId")
	Collection<BookingRecord> findAllBookingPassengerByCustomerId(int customerId);

	@Query("select bp from BookingRecord bp where bp.id =:bpId")
	BookingRecord findBookingPassengerById(int bpId);

	@Query("select bk from Booking bk where bk.customer.id = :id")
	Collection<Booking> findBookingByCustomerId(int id);

	@Query("select p from Passenger p where p.customer.id = :customerId")
	Collection<Passenger> findPassengerByCustomerId(int customerId);

	@Query("select bp.passenger from BookingRecord bp where bp.booking.id = :bookingId")
	Collection<Passenger> findPassengersByBookingId(int bookingId);

	@Query("select count(bp) from BookingRecord bp where bp.booking.id = :bookingId and bp.passenger.id = :passengerId")
	int existsBookingPassenger(int bookingId, int passengerId);

	@Query("select b from Booking b where b.id =:bookingId")
	Booking findBookingById(int bookingId);

	@Query("select count(bp) from BookingRecord bp where bp.passenger.id = :passengerId and bp.booking.customer.id = :customerId")
	int existsBookingPassengerForCustomer(int passengerId, int customerId);

	@Query("select count(bp) from BookingRecord bp where bp.booking.id = :bookingId")
	int countByBookingId(int bookingId);

	@Query("select bp from BookingRecord bp where bp.booking.id = :bookingId and bp.passenger.draftMode = false")
	List<BookingRecord> findPublishedByBookingId(int bookingId);

	@Query("select c from Customer c where c.id = :customerId")
	Customer findCustomerById(Integer customerId);

}
