package com.medicalSolutionsInc.mappers.notificationMapper;

import com.medicalSolutionsInc.dto.notificationDTO.CreateNotificationRequestDTO;
import com.medicalSolutionsInc.dto.notificationDTO.CreateNotificationResponseDTO;
import com.medicalSolutionsInc.entity.notification.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface NotificationMapper {
		
		@Mapping(target = "id", ignore = true)
		@Mapping(target = "status", ignore = true)
		@Mapping(target = "sentAt", ignore = true)
		@Mapping(target = "deliveredAt", ignore = true)
		@Mapping(target = "readAt", ignore = true)
		@Mapping(target = "retryCount", ignore = true)
		@Mapping(target = "maxRetries", ignore = true)
		@Mapping(target = "failureReason", ignore = true)
		@Mapping(target = "read", ignore = true)
		@Mapping(target = "archived", ignore = true)
		@Mapping(target = "createdAt", ignore = true)
		@Mapping(target = "updatedAt", ignore = true)
		Notification toEntity(CreateNotificationRequestDTO dto);
		
		CreateNotificationResponseDTO toResponseDTO(Notification notification);
}