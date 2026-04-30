package com.medicalSolutionsInc.utils.bookingSpecification;

import com.medicalSolutionsInc.enumerations.booking.BookingStatus;
import com.medicalSolutionsInc.enumerations.booking.EmploymentStatus;
import com.medicalSolutionsInc.enumerations.booking.MaritalStatus;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BookingSpecification {

		private final List<Criteria> criteriaList = new ArrayList<>();
		
		public BookingSpecification firstName(String firstName) {
			if (isPresent(firstName))
				criteriaList.add(Criteria.where("firstName").regex(firstName, "i"));
			return this;
		}
		
		public BookingSpecification lastName(String lastName) {
			if (isPresent(lastName))
				criteriaList.add(Criteria.where("lastName").regex(lastName, "i"));
			return this;
		}
		
		public BookingSpecification email(String email) {
			if (isPresent(email))
				criteriaList.add(Criteria.where("email").regex(email, "i"));
			return this;
		}
		
		public BookingSpecification phone(String phone) {
			if (isPresent(phone))
				criteriaList.add(Criteria.where("phone").is(phone));
			return this;
		}
		
		public BookingSpecification city(String city) {
			if (isPresent(city))
				criteriaList.add(Criteria.where("city").regex(city, "i"));
			return this;
		}
		
		public BookingSpecification state(String state) {
			if (isPresent(state))
				criteriaList.add(Criteria.where("state").regex(state, "i"));
			return this;
		}
		
		public BookingSpecification country(String country) {
			if (isPresent(country))
				criteriaList.add(Criteria.where("country").regex(country, "i"));
			return this;
		}
		
		public BookingSpecification zipCode(String zipCode) {
			if (isPresent(zipCode))
				criteriaList.add(Criteria.where("zipCode").is(zipCode));
			return this;
		}
		
		public BookingSpecification profession(String profession) {
			if (isPresent(profession))
				criteriaList.add(Criteria.where("profession").regex(profession, "i"));
			return this;
		}
		
		
		public BookingSpecification bookingStatus(BookingStatus bookingStatus) {
			if (bookingStatus != null)
				criteriaList.add(Criteria.where("bookingStatus").is(bookingStatus));
			return this;
		}
		
		public BookingSpecification maritalStatus(MaritalStatus maritalStatus) {
			if (maritalStatus != null)
				criteriaList.add(Criteria.where("maritalStatus").is(maritalStatus));
			return this;
		}
		
		public BookingSpecification employmentStatus(EmploymentStatus employmentStatus) {
			if (employmentStatus != null)
				criteriaList.add(Criteria.where("employmentStatus").is(employmentStatus));
			return this;
		}
		
		public BookingSpecification ageBetween(Integer minAge, Integer maxAge) {
			if (minAge != null && maxAge != null)
				criteriaList.add(Criteria.where("age").gte(minAge).lte(maxAge));
			else if (minAge != null)
				criteriaList.add(Criteria.where("age").gte(minAge));
			else if (maxAge != null)
				criteriaList.add(Criteria.where("age").lte(maxAge));
			return this;
		}
		
		
		public BookingSpecification createdBetween(Instant from, Instant to) {
			if (from != null && to != null)
				criteriaList.add(Criteria.where("createdAt").gte(from).lte(to));
			else if (from != null)
				criteriaList.add(Criteria.where("createdAt").gte(from));
			else if (to != null)
				criteriaList.add(Criteria.where("createdAt").lte(to));
			return this;
		}
		
		public BookingSpecification updatedBetween(Instant from, Instant to) {
			if (from != null && to != null)
				criteriaList.add(Criteria.where("updatedAt").gte(from).lte(to));
			else if (from != null)
				criteriaList.add(Criteria.where("updatedAt").gte(from));
			else if (to != null)
				criteriaList.add(Criteria.where("updatedAt").lte(to));
			return this;
		}
		
		public BookingSpecification hasAllergy(String allergy) {
			if (isPresent(allergy))
				criteriaList.add(Criteria.where("allergies").in(allergy));
			return this;
		}
		
		public BookingSpecification hasDisease(String disease) {
			if (isPresent(disease))
				criteriaList.add(Criteria.where("diseases").in(disease));
			return this;
		}
		
		public BookingSpecification hasSymptom(String symptom) {
			if (isPresent(symptom))
				criteriaList.add(Criteria.where("symptoms").in(symptom));
			return this;
		}
		
		public BookingSpecification hasMedication(String medication) {
			if (isPresent(medication))
				criteriaList.add(Criteria.where("previousMedications").in(medication));
			return this;
		}
		
		public BookingSpecification hasHobby(String hobby) {
			if (isPresent(hobby))
				criteriaList.add(Criteria.where("hobbies").in(hobby));
			return this;
		}
		
		public BookingSpecification hasAcademicLevel(String academicLevel) {
			if (isPresent(academicLevel))
				criteriaList.add(Criteria.where("academicLevel").in(academicLevel));
			return this;
		}
		
		
		public BookingSpecification keyword(String keyword) {
			if (isPresent(keyword)) {
				criteriaList.add(new Criteria().orOperator(
						Criteria.where("firstName").regex(keyword, "i"),
						Criteria.where("lastName").regex(keyword, "i"),
						Criteria.where("email").regex(keyword, "i"),
						Criteria.where("phone").regex(keyword, "i"),
						Criteria.where("city").regex(keyword, "i"),
						Criteria.where("state").regex(keyword, "i"),
						Criteria.where("country").regex(keyword, "i"),
						Criteria.where("profession").regex(keyword, "i"),
						Criteria.where("address").regex(keyword, "i")
				));
			}
			return this;
		}
		
		
		public Criteria build() {
			if (criteriaList.isEmpty()) return new Criteria();
			return new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
		}
		
		
		private boolean isPresent(String value) {
			return value != null && !value.isBlank();
		}
}