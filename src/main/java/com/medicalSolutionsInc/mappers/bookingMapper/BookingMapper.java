package com.medicalSolutionsInc.mappers.bookingMapper;

import com.medicalSolutionsInc.dto.bookingDTO.*;
import com.medicalSolutionsInc.entity.booking.Booking;
import org.mapstruct.*;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookingMapper {

// ✅ CREATE
@Mapping(target = "id", ignore = true)
@Mapping(target = "createdAt", ignore = true)
@Mapping(target = "updatedAt", ignore = true)
@Mapping(target = "bookingStatus", ignore = true)

// NEW FIELDS (explicit for clarity & safety)
@Mapping(target = "gender", source = "gender")
@Mapping(target = "emergencyContactName", source = "emergencyContactName")
@Mapping(target = "emergencyContactPhone", source = "emergencyContactPhone")
@Mapping(target = "insuranceProvider", source = "insuranceProvider")
@Mapping(target = "insuranceNumber", source = "insuranceNumber")
@Mapping(target = "notes", source = "notes")

Booking toEntity(BookingRequestDTO dto);


// ✅ RESPONSE
@Mapping(target = "gender", source = "gender")
@Mapping(target = "emergencyContactName", source = "emergencyContactName")
@Mapping(target = "emergencyContactPhone", source = "emergencyContactPhone")
@Mapping(target = "insuranceProvider", source = "insuranceProvider")
@Mapping(target = "insuranceNumber", source = "insuranceNumber")
@Mapping(target = "notes", source = "notes")

BookingResponseDTO toResponseDTO(Booking booking);


// ✅ SEARCH RESPONSE (include if needed)
@Mapping(target = "gender", source = "gender")
@Mapping(target = "emergencyContactName", source = "emergencyContactName")
@Mapping(target = "emergencyContactPhone", source = "emergencyContactPhone")
@Mapping(target = "insuranceProvider", source = "insuranceProvider")
@Mapping(target = "insuranceNumber", source = "insuranceNumber")
@Mapping(target = "notes", source = "notes")

SearchBookingResponseDTO toSearchResponseDTO(Booking booking);


// ✅ UPDATE (partial update — respects IGNORE nulls)
@Mapping(target = "id", ignore = true)
@Mapping(target = "createdAt", ignore = true)
@Mapping(target = "updatedAt", ignore = true)

@Mapping(target = "gender", source = "gender")
@Mapping(target = "emergencyContactName", source = "emergencyContactName")
@Mapping(target = "emergencyContactPhone", source = "emergencyContactPhone")
@Mapping(target = "insuranceProvider", source = "insuranceProvider")
@Mapping(target = "notes", source = "notes")

void updateEntityFromDTO(UpdateBookingResponseDTO dto, @MappingTarget Booking booking);
}