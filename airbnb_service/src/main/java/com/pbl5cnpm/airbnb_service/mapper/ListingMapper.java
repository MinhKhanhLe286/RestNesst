package com.pbl5cnpm.airbnb_service.mapper;

import com.pbl5cnpm.airbnb_service.dto.Response.AmenitiesForListingRespose;
import com.pbl5cnpm.airbnb_service.dto.Response.ListingDetailResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.ListingFavorite;
import com.pbl5cnpm.airbnb_service.dto.Response.ListingsResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.ReviewResponse;
import com.pbl5cnpm.airbnb_service.entity.ListingEntity;
import com.pbl5cnpm.airbnb_service.entity.ReviewEntity;
import com.pbl5cnpm.airbnb_service.entity.AmenitesEntity;
import com.pbl5cnpm.airbnb_service.entity.ImagesEntity;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ListingMapper {

    @Mappings({
            @Mapping(source = "title", target = "name"),
            @Mapping(source = "countriesEntity.name", target = "country"),
            @Mapping(source = "imagesEntities", target = "images", qualifiedByName = "mapImages"),
            @Mapping(source = "host.thumnailUrl", target = "hostThumnailUrl")
    })
    ListingsResponse toResponse(ListingEntity entity);

    @Mappings({
            @Mapping(source = "countriesEntity.name", target = "country"),
            @Mapping(source = "imagesEntities", target = "images", qualifiedByName = "mapImages"),
            @Mapping(source = "amenitesEntities", target = "amenites", qualifiedByName = "mapAmenites"),
            @Mapping(source = "reviews", target = "reviews", qualifiedByName = "mapReviews")
    })
    ListingDetailResponse toDetailResponse(ListingEntity entity);

    @Named("mapImages")
    default List<String> mapImages(List<ImagesEntity> imagesEntities) {
        if (imagesEntities == null)
            return null;
        return imagesEntities.stream()
                .map(ImagesEntity::getImageUrl)
                .collect(Collectors.toList());
    }

    @Named("mapAmenites")
    default List<AmenitiesForListingRespose> mapAmenites(List<AmenitesEntity> amenitesEntities) {
        if (amenitesEntities == null)
            return null;
        return amenitesEntities.stream()
                .map(amenitesEntitie -> AmenitiesForListingRespose.builder()
                        .name(amenitesEntitie.getName())
                        .thumnailUrl(amenitesEntitie.getThumnailUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Named("mapReviews")
    default List<ReviewResponse> mapReviews(List<ReviewEntity> reviewEntities) {
        if (reviewEntities == null)
            return null;
        List<ReviewResponse> result = new ArrayList<>();
        for (int i = 0; i < reviewEntities.size(); i++) {
            ReviewEntity reviewEntity = reviewEntities.get(i);
            ReviewResponse res = ReviewResponse.builder()
                    .username(reviewEntity.getUserEntity().getUsername())
                    .comment(reviewEntity.getComment())
                    .rating(reviewEntity.getRating())
                    .reviewDate(reviewEntity.getReviewDate())
                    .thumnailUrl(reviewEntity.getUserEntity().getThumnailUrl())
                    .build();
            result.add(res);
        }
        return result;
    } //// new 
    @Mappings({
        @Mapping(source = "imagesEntities", target = "primaryThumnail", qualifiedByName = "primaryThumnail")
    })
    ListingFavorite toLitingFavorite(ListingEntity listingEntity);

    @Named("primaryThumnail")
    default String primaryThumnail(List<ImagesEntity> imagesEntities){
        if(imagesEntities == null) return null;
        return imagesEntities.get(0).getImageUrl();
    }
}
