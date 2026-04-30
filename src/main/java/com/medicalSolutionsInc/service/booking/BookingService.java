package com.medicalSolutionsInc.service.booking;

import com.medicalSolutionsInc.config.cloudinaryConfig.CloudinaryConfig;
import com.medicalSolutionsInc.dto.bookingDTO.*;
import com.medicalSolutionsInc.entity.booking.Booking;
import com.medicalSolutionsInc.enumerations.booking.BookingStatus;
import com.medicalSolutionsInc.enumerations.booking.EmploymentStatus;
import com.medicalSolutionsInc.enumerations.booking.MaritalStatus;
import com.medicalSolutionsInc.exceptions.badRequestException.BadRequestException;
import com.medicalSolutionsInc.mappers.bookingMapper.BookingMapper;
import com.medicalSolutionsInc.repository.booking.BookingRepository;
import com.medicalSolutionsInc.repository.booking.BookingSearchRepository;
import com.medicalSolutionsInc.utils.bookingSpecification.BookingSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {

			private final BookingRepository bookingRepository;
			private final CloudinaryConfig cloudinaryConfig;
			private final BookingMapper bookingMapper;
			private final BookingSearchRepository bookingSearchRepository;
			
			public BookingResponseDTO createBooking(BookingRequestDTO dto, MultipartFile image) throws Exception {
				log.info("Creating booking for: {} {}", dto.firstName(), dto.lastName());
				
				if (bookingRepository.existsByEmail(dto.email())) {
					throw new BadRequestException("A booking with email " + dto.email() + " already exists");
				}
				
				Booking booking = bookingMapper.toEntity(dto);
				booking.setBookingStatus(BookingStatus.CONFIRMED);
				booking.setCreatedAt(Instant.now());
				booking.setUpdatedAt(Instant.now());
				
				if (image != null && !image.isEmpty()) {
					String imageUrl = cloudinaryConfig.uploadProfilePicture(image, UUID.randomUUID());
					booking.setImageURL(imageUrl);
					log.info("Profile image uploaded to Cloudinary for booking: {}", dto.email());
				}
				
				Booking saved = bookingRepository.save(booking);
				log.info("Booking created with ID: {}", saved.getId());
				return bookingMapper.toResponseDTO(saved);
			}
			
			public Page<SearchBookingResponseDTO> fetchBookings(int page, int size, String sortBy, String sortDir) {
				log.info("Fetching all bookings — page={}, size={}, sortBy={}, sortDir={}", page, size, sortBy, sortDir);
				
				Sort sort = sortDir.equalsIgnoreCase("asc")
						            ? Sort.by(sortBy).ascending()
						            : Sort.by(sortBy).descending();
				
				return bookingRepository
						       .findAll(PageRequest.of(page, size, sort))
						       .map(bookingMapper::toSearchResponseDTO);
			}
			
			public BookingResponseDTO fetchBooking(String id) throws Exception {
				log.info("Fetching booking with ID: {}", id);
				Booking booking = findBookingOrThrow(id);
				return bookingMapper.toResponseDTO(booking);
			}
			
			public BookingResponseDTO updateBooking(String id, UpdateBookingResponseDTO dto, MultipartFile image) throws Exception {
				log.info("Updating booking with ID: {}", id);
				
				Booking booking = findBookingOrThrow(id);
				
				if (image != null && !image.isEmpty()) {
					if (booking.getImageURL() != null && !booking.getImageURL().isBlank()) {
						cloudinaryConfig.deleteByUrl(booking.getImageURL());
						log.info("Old profile image deleted from Cloudinary for booking: {}", id);
					}
					String newImageUrl = cloudinaryConfig.uploadProfilePicture(image, UUID.randomUUID());
					booking.setImageURL(newImageUrl);
					log.info("New profile image uploaded to Cloudinary for booking: {}", id);
				}
				
				bookingMapper.updateEntityFromDTO(dto, booking);
				booking.setUpdatedAt(Instant.now());
				
				Booking updated = bookingRepository.save(booking);
				log.info("Booking updated: {}", updated.getId());
				return bookingMapper.toResponseDTO(updated);
			}
			
			public void deleteBooking(String id) throws Exception {
				log.info("Deleting booking with ID: {}", id);
				Booking booking = findBookingOrThrow(id);
				
				if (booking.getImageURL() != null && !booking.getImageURL().isBlank()) {
					cloudinaryConfig.deleteByUrl(booking.getImageURL());
					log.info("Profile image deleted from Cloudinary for booking: {}", id);
				}
				
				bookingRepository.delete(booking);
				log.info("Booking deleted: {}", id);
			}
			
			public Page<SearchBookingResponseDTO> searchBookings(
					String keyword,
					String firstName,
					String lastName,
					String email,
					String phone,
					String city,
					String state,
					String country,
					String zipCode,
					String profession,
					BookingStatus bookingStatus,
					MaritalStatus maritalStatus,
					EmploymentStatus employmentStatus,
					Integer minAge,
					Integer maxAge,
					String hasAllergy,
					String hasDisease,
					String hasSymptom,
					Instant createdFrom,
					Instant createdTo,
					int page,
					int size,
					String sortBy,
					String sortDir
			) {
				log.info("Searching bookings with filters — keyword={}, status={}, page={}, size={}", keyword, bookingStatus, page, size);
				
				Sort sort = sortDir.equalsIgnoreCase("asc")
						            ? Sort.by(sortBy).ascending()
						            : Sort.by(sortBy).descending();
				
				BookingSpecification specification = new BookingSpecification ()
						                                     .keyword(keyword)
						                                     .firstName(firstName)
						                                     .lastName(lastName)
						                                     .email(email)
						                                     .phone(phone)
						                                     .city(city)
						                                     .state(state)
						                                     .country(country)
						                                     .zipCode(zipCode)
						                                     .profession(profession)
						                                     .bookingStatus(bookingStatus)
						                                     .maritalStatus(maritalStatus)
						                                     .employmentStatus(employmentStatus)
						                                     .ageBetween(minAge, maxAge)
						                                     .hasAllergy(hasAllergy)
						                                     .hasDisease(hasDisease)
						                                     .hasSymptom(hasSymptom)
						                                     .createdBetween(createdFrom, createdTo);
				
				return bookingSearchRepository
						       .search(specification, PageRequest.of(page, size, sort))
						       .map(bookingMapper::toSearchResponseDTO);
			}
			
			public long countTotalBookings() {
				long count = bookingRepository.count();
				log.info("Total bookings count: {}", count);
				return count;
			}
			
			private Booking findBookingOrThrow(String id) throws Exception {
				return bookingRepository.findById(id)
						       .orElseThrow(() -> new BadRequestException("Booking not found with ID: " + id));
			}
}