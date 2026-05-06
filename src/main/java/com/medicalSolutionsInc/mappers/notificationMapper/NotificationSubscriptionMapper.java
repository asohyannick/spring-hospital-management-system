package com.medicalSolutionsInc.mappers.notificationMapper;

import com.medicalSolutionsInc.dto.notificationDTO.SubscriptionRequestDTO;
import com.medicalSolutionsInc.dto.notificationDTO.SubscriptionResponseDTO;
import com.medicalSolutionsInc.entity.notification.NotificationSubscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface NotificationSubscriptionMapper {

@Mapping(target = "id", ignore = true)
@Mapping(target = "emailVerified", ignore = true)
@Mapping(target = "phoneVerified", ignore = true)
@Mapping(target = "active", ignore = true)
@Mapping(target = "createdAt", ignore = true)
@Mapping(target = "updatedAt", ignore = true)
@Mapping(target = "deletedAt", ignore = true)
NotificationSubscription toEntity(SubscriptionRequestDTO dto);

SubscriptionResponseDTO toResponseDTO(NotificationSubscription subscription);

@Mapping(target = "id", ignore = true)
@Mapping(target = "emailVerified", ignore = true)
@Mapping(target = "phoneVerified", ignore = true)
@Mapping(target = "active", ignore = true)
@Mapping(target = "createdAt", ignore = true)
@Mapping(target = "updatedAt", ignore = true)
@Mapping(target = "deletedAt", ignore = true)
void updateEntity(SubscriptionRequestDTO dto, @MappingTarget NotificationSubscription subscription);
}