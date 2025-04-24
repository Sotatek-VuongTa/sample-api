package com.sample.api.mapper;

import com.sample.api.dto.AdminDto;
import com.sample.api.dto.AdminDto.CreateRequestDto;
import com.sample.api.dto.BrandDto;
import com.sample.api.dto.CategoryDto;
import com.sample.api.dto.ProductDto;
import com.sample.api.entity.Brand;
import com.sample.api.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class AdminMapper {
    public static List<ProductDto> mapToProductDtoList(CreateRequestDto request) {
        List<ProductDto> products = new ArrayList<>();

        products.add(ProductDto.builder().category("Tops").cost(request.getTopPrice()).build());
        products.add(ProductDto.builder().category("Jackets").cost(request.getJacketPrice()).build());
        products.add(ProductDto.builder().category("Sneakers").cost(request.getSneakerPrice()).build());
        products.add(ProductDto.builder().category("Bags").cost(request.getBagPrice()).build());
        products.add(ProductDto.builder().category("Hats").cost(request.getHatPrice()).build());
        products.add(ProductDto.builder().category("Socks").cost(request.getSockPrice()).build());
        products.add(ProductDto.builder().category("Accessories").cost(request.getAccessoryPrice()).build());

        return products;
    }

    public static AdminDto.CreateResponseDto fromBrandToCreateResponse(Brand brand) {
        return AdminDto.CreateResponseDto.builder()
                .message("created brand successfully")
                .brand(BrandDto.builder()
                        .brandName(brand.getBrandName())
                        .categories(brand.getCategories().stream().map(category ->
                                        CategoryDto.builder()
                                                .categoryName(category.getCategoryName())
                                                .cost(category.getCost())
                                                .build())
                                .toList())
                        .build())
                .build();
    }
}
