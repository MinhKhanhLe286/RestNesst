package com.pbl5cnpm.airbnb_service.mapper;

import org.mapstruct.Mapper;

import com.pbl5cnpm.airbnb_service.dto.Response.AmenitiesResponse;
import com.pbl5cnpm.airbnb_service.entity.AmenitesEntity;

@Mapper(componentModel = "spring")
public interface AmenitiesMapper {
    AmenitiesResponse toAmenitiesResponse(AmenitesEntity amenitesEntity);
    
}
