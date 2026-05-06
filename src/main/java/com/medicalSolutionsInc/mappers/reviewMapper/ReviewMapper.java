package com.medicalSolutionsInc.mappers.reviewMapper;

import com.medicalSolutionsInc.dto.reviewDTO.CreateReviewRequestDTO;
import com.medicalSolutionsInc.dto.reviewDTO.CreateReviewResponseDTO;
import com.medicalSolutionsInc.entity.review.Review;
import org.mapstruct.*;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ReviewMapper {

		@Mapping(target = "id",            ignore = true)
		@Mapping(target = "reviewNumber",  ignore = true)
		@Mapping(target = "status",        ignore = true)
		@Mapping(target = "helpfulVotes",  ignore = true)
		@Mapping(target = "reportedCount", ignore = true)
		@Mapping(target = "attachments",   ignore = true)
		@Mapping(target = "response",      ignore = true)
		@Mapping(target = "featured",      ignore = true)
		@Mapping(target = "edited",        ignore = true)
		@Mapping(target = "editedAt",      ignore = true)
		@Mapping(target = "createdAt",     ignore = true)
		@Mapping(target = "updatedAt",     ignore = true)
		@Mapping(target = "deletedAt",     ignore = true)
		Review toEntity(CreateReviewRequestDTO dto);
		
		CreateReviewResponseDTO toResponseDTO(Review review);
		
		@Mapping(target = "id",            ignore = true)
		@Mapping(target = "reviewNumber",  ignore = true)
		@Mapping(target = "status",        ignore = true)
		@Mapping(target = "helpfulVotes",  ignore = true)
		@Mapping(target = "reportedCount", ignore = true)
		@Mapping(target = "attachments",   ignore = true)
		@Mapping(target = "response",      ignore = true)
		@Mapping(target = "featured",      ignore = true)
		@Mapping(target = "edited",        ignore = true)
		@Mapping(target = "editedAt",      ignore = true)
		@Mapping(target = "createdAt",     ignore = true)
		@Mapping(target = "updatedAt",     ignore = true)
		@Mapping(target = "deletedAt",     ignore = true)
		void updateEntityFromDTO(CreateReviewRequestDTO dto, @MappingTarget Review review);
}