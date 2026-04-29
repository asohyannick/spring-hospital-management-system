package com.medicalSolutionsInc.mappers.userMapper.auth;

import com.medicalSolutionsInc.dto.userDTO.LoginRequestDTO;
import com.medicalSolutionsInc.dto.userDTO.RegistrationRequestDTO;
import com.medicalSolutionsInc.dto.userDTO.UserResponseDTO;
import com.medicalSolutionsInc.entity.user.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

		@Mapping(target = "id",                   ignore = true)
		@Mapping(target = "password",             ignore = true)
		@Mapping(target = "active",               ignore = true)
		@Mapping(target = "accountBlocked",       ignore = true)
		@Mapping(target = "accessToken",          ignore = true)
		@Mapping(target = "refreshToken",         ignore = true)
		@Mapping(target = "avatarUrl",            ignore = true)
		@Mapping(target = "failedLoginAttempts",  ignore = true)
		@Mapping(target = "twoFactorAttempts",    ignore = true)
		@Mapping(target = "magicLinkToken",       ignore = true)
		@Mapping(target = "magicLinkTokenExpiration", ignore = true)
		@Mapping(target = "accountVerified",      ignore = true)
		@Mapping(target = "accountActive",        ignore = true)
		@Mapping(target = "createdAt",            ignore = true)
		@Mapping(target = "updatedAt",            ignore = true)
		User toEntity(RegistrationRequestDTO request);
		
		@Mapping(target = "id",                   ignore = true)
		@Mapping(target = "firstName",            ignore = true)
		@Mapping(target = "lastName",             ignore = true)
		@Mapping(target = "role",                 ignore = true)
		@Mapping(target = "active",               ignore = true)
		@Mapping(target = "accountBlocked",       ignore = true)
		@Mapping(target = "accessToken",          ignore = true)
		@Mapping(target = "refreshToken",         ignore = true)
		@Mapping(target = "avatarUrl",            ignore = true)
		@Mapping(target = "failedLoginAttempts",  ignore = true)
		@Mapping(target = "twoFactorAttempts",    ignore = true)
		@Mapping(target = "magicLinkToken",       ignore = true)
		@Mapping(target = "magicLinkTokenExpiration", ignore = true)
		@Mapping(target = "accountVerified",      ignore = true)
		@Mapping(target = "accountActive",        ignore = true)
		@Mapping(target = "createdAt",            ignore = true)
		@Mapping(target = "updatedAt",            ignore = true)
		User toEntity(LoginRequestDTO request);
		
		UserResponseDTO toResponse(User user);
		
		List<UserResponseDTO> toResponseList(List<User> users);
		
		@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
		@Mapping(target = "id",                   ignore = true)
		@Mapping(target = "password",             ignore = true)
		@Mapping(target = "active",               ignore = true)
		@Mapping(target = "accountBlocked",       ignore = true)
		@Mapping(target = "accessToken",          ignore = true)
		@Mapping(target = "refreshToken",         ignore = true)
		@Mapping(target = "avatarUrl",            ignore = true)
		@Mapping(target = "failedLoginAttempts",  ignore = true)
		@Mapping(target = "twoFactorAttempts",    ignore = true)
		@Mapping(target = "magicLinkToken",       ignore = true)
		@Mapping(target = "magicLinkTokenExpiration", ignore = true)
		@Mapping(target = "accountVerified",      ignore = true)
		@Mapping(target = "accountActive",        ignore = true)
		@Mapping(target = "createdAt",            ignore = true)
		@Mapping(target = "updatedAt",            ignore = true)
		void updateEntityFromDTO(RegistrationRequestDTO request, @MappingTarget User user);
}