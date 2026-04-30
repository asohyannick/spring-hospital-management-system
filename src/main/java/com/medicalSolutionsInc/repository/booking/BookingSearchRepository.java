package com.medicalSolutionsInc.repository.booking;

import com.medicalSolutionsInc.entity.booking.Booking;
import com.medicalSolutionsInc.utils.bookingSpecification.BookingSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookingSearchRepository {

	private final MongoTemplate mongoTemplate;
	
	public Page<Booking> search(BookingSpecification specification, Pageable pageable) {
		Criteria criteria = specification.build();
		
		Query countQuery = new Query(criteria);
		long total = mongoTemplate.count(countQuery, Booking.class);
		
		Query dataQuery = new Query(criteria)
				                  .with(pageable);
		
		List<Booking> bookings = mongoTemplate.find(dataQuery, Booking.class);
		
		return new PageImpl<>(bookings, pageable, total);
	}
}