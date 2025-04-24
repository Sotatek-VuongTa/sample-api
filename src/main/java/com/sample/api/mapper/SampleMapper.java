package com.sample.api.mapper;

import com.sample.api.dto.ProductDto;
import com.sample.api.entity.Category;

import java.util.List;

public class SampleMapper {

    public static List<ProductDto> fromCategoryListToProductList(List<Category> categories) {
        return categories.stream().map(c -> ProductDto.builder()
                .cost(c.getCost())
                .brand(c.getBrand().getBrandName())
                .build()).toList();
    }
}
