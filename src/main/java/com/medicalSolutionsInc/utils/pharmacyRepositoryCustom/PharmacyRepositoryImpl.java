package com.medicalSolutionsInc.utils.pharmacyRepositoryCustom;

import com.medicalSolutionsInc.entity.pharmacy.Pharmacy;
import com.medicalSolutionsInc.enumerations.pharmacyStatus.PharmacyStatus;
import com.medicalSolutionsInc.enumerations.pharmacyType.PharmacyType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class PharmacyRepositoryImpl implements PharmacyRepositoryCustom {

private final MongoTemplate mongoTemplate;

@Override
public Page < Pharmacy > search(
		String keyword,
		PharmacyType type,
		PharmacyStatus status,
		Boolean verified,
		Boolean open24Hours,
		Boolean offersDelivery,
		Boolean acceptsOnlineOrders,
		Instant from,
		Instant to,
		Pageable pageable
) {
	Criteria criteria = Criteria.where("deletedAt").isNull();
	
	if ( StringUtils.hasText(keyword)) {
		Pattern pattern = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
		criteria.andOperator(
				new Criteria().orOperator(
						Criteria.where("name").regex(pattern),
						Criteria.where("email").regex(pattern),
						Criteria.where("licenseNumber").regex(pattern),
						Criteria.where("pharmacyNumber").regex(pattern),
						Criteria.where("headPharmacist").regex(pattern)
				)
		);
	}
	
	if (type != null)                criteria.and("type").is(type);
	if (status != null)              criteria.and("status").is(status);
	if (verified != null)            criteria.and("verified").is(verified);
	if (open24Hours != null)         criteria.and("open24Hours").is(open24Hours);
	if (offersDelivery != null)      criteria.and("offersDelivery").is(offersDelivery);
	if (acceptsOnlineOrders != null) criteria.and("acceptsOnlineOrders").is(acceptsOnlineOrders);
	
	if (from != null && to != null) {
		criteria.and("createdAt").gte(from).lte(to);
	} else if (from != null) {
		criteria.and("createdAt").gte(from);
	} else if (to != null) {
		criteria.and("createdAt").lte(to);
	}
	
	Query query = new Query(criteria).with(pageable);
	
	List <Pharmacy> results = mongoTemplate.find(query, Pharmacy.class);
	long total = mongoTemplate.count( Query.of(query).limit(-1).skip(-1), Pharmacy.class);
	
	return new PageImpl <> (results, pageable, total);
}
}