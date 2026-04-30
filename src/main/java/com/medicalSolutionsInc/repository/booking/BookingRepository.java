// BookingRepository.java
package com.medicalSolutionsInc.repository.booking;

import com.medicalSolutionsInc.entity.booking.Booking;
import com.medicalSolutionsInc.enumerations.booking.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
	
	boolean existsByEmail(String email);
	
	Page<Booking> findByBookingStatus(BookingStatus bookingStatus, Pageable pageable);
	
	Page<Booking> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
			String firstName, String lastName, Pageable pageable
	);
	
	Page<Booking> findByCityAndCountry(String city, String country, Pageable pageable);
	
	List<Booking> findByCreatedAtBetween(Instant from, Instant to);
	
	java.util.Optional<Booking> findByEmail(String email);
	
	@Query("{ $or: [ " +
			       "{ 'firstName': { $regex: ?0, $options: 'i' } }, " +
			       "{ 'lastName':  { $regex: ?0, $options: 'i' } }, " +
			       "{ 'city':      { $regex: ?0, $options: 'i' } }, " +
			       "{ 'country':   { $regex: ?0, $options: 'i' } }, " +
			       "{ 'profession':{ $regex: ?0, $options: 'i' } } " +
			       "] }")
	Page<Booking> searchByKeyword(String keyword, Pageable pageable);
}