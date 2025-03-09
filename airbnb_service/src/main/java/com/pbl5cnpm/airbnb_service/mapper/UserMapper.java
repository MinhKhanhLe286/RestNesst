package com.pbl5cnpm.airbnb_service.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pbl5cnpm.airbnb_service.dto.Request.UserRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.UserResponse;
import com.pbl5cnpm.airbnb_service.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toUserEntity(UserRequest request);

    @Mapping(target = "roles", expression = "java(mapRoles(userEntity))")
    UserResponse toUserResponse(UserEntity userEntity);

    default Set<String> mapRoles(UserEntity userEntity) {
        return userEntity.getRoles()
                .stream()
                .map(role -> role.getRoleName()) // Lấy tên Role
                .collect(Collectors.toSet());
    }
}
