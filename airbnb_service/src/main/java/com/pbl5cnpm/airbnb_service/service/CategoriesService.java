package com.pbl5cnpm.airbnb_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pbl5cnpm.airbnb_service.dto.Request.CategoriesRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.CategoriesResponse;
import com.pbl5cnpm.airbnb_service.entity.CategoriesEntity;
import com.pbl5cnpm.airbnb_service.mapper.CategoriesMapper;
import com.pbl5cnpm.airbnb_service.repository.CategoriesRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CategoriesService {
    CategoriesRepository categoriesRepository;
    CategoriesMapper categoriesMapper;

    public CategoriesResponse handleCreateCategories(CategoriesRequest categoriesRequest){
        CategoriesEntity categoriesEntity = categoriesMapper.toCategoriesEntity(categoriesRequest);
                    categoriesEntity.setDeleted(false);
        return this.categoriesMapper.tCategoriesResponse(this.categoriesRepository.save(categoriesEntity));
    }

    public List<CategoriesResponse> handleFindAll(){
        return this.categoriesRepository
                    .findAll()
                    .stream()
                    .map(category -> categoriesMapper.tCategoriesResponse(category) )
                    .toList();
    }
}
