package com.pbl5cnpm.airbnb_service.mapper;

import org.mapstruct.Mapper;

import com.pbl5cnpm.airbnb_service.dto.Request.CoutriesRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.CoutriesResponse;
import com.pbl5cnpm.airbnb_service.entity.CountriesEntity;

@Mapper(componentModel = "spring")
public interface CountriesMapper {
    CoutriesResponse toCoutriesResponse(CountriesEntity coutriesEntity);
    CountriesEntity toCoutriesEntity(CoutriesRequest coutriesRequest);
}
