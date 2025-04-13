package com.pbl5cnpm.airbnb_service.mapper;

import com.pbl5cnpm.airbnb_service.dto.Response.ListingsResponse;
import com.pbl5cnpm.airbnb_service.entity.ListingEntity;
import com.pbl5cnpm.airbnb_service.entity.ImagesEntity;
import org.mapstruct.*;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ListingMapper {

    @Mappings({
        @Mapping(source = "title", target = "name"),
        @Mapping(source = "countriesEntity.name", target = "country"),
        @Mapping(source = "imagesEntities", target = "images", qualifiedByName = "mapImages")
    })
    ListingsResponse toResponse(ListingEntity entity);

    @Named("mapImages")
    default List<String> mapImages(List<ImagesEntity> imagesEntities) {
        if (imagesEntities == null) return null;
        return imagesEntities.stream()
            .map(ImagesEntity::getImageUrl) 
            .collect(Collectors.toList());
    }
}
