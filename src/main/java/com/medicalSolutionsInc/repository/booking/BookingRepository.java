package com.medicalSolutionsInc.repository.booking;

import com.medicalSolutionsInc.entity.booking.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingRepository extends MongoRepository<Booking, String> {
	
	boolean existsByEmail(String email);
}