package com.pbl5cnpm.airbnb_service.mapper;

import org.mapstruct.Mapper;

import com.pbl5cnpm.airbnb_service.dto.Request.CategoriesRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.CategoriesResponse;
import com.pbl5cnpm.airbnb_service.entity.CategoriesEntity;

@Mapper(componentModel = "spring")
public interface CategoriesMapper {
    CategoriesEntity toCategoriesEntity(CategoriesRequest categoriesRequest);
    CategoriesResponse tCategoriesResponse (CategoriesEntity categoriesEntity);
}
