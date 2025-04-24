package com.sample.api.service.impl;

import com.sample.api.dto.CategoryDto;
import com.sample.api.dto.ProductDto;
import com.sample.api.dto.SampleDto.PriceRangeResponse;
import com.sample.api.dto.SampleDto.LowestPriceDto;
import com.sample.api.dto.SampleDto.LowestPriceResponse;
import com.sample.api.dto.SampleDto.CheapestComboResponse;
import com.sample.api.entity.Brand;
import com.sample.api.entity.Category;
import com.sample.api.mapper.SampleMapper;
import com.sample.api.projection.CheapestComboProjection;
import com.sample.api.projection.LowestPriceProjection;
import com.sample.api.repository.BrandRepository;
import com.sample.api.repository.CategoryRepository;
import com.sample.api.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SampleServiceImpl implements SampleService {

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public CheapestComboResponse getCheapestCombo() {
        List<CheapestComboProjection> products
                = categoryRepository.findCheapestCombo();

        return CheapestComboResponse.builder()
                .totalCost(products.stream()
                        .mapToLong(CheapestComboProjection::getCost)
                        .sum())
                .products(products.stream().map(
                        productProjection -> ProductDto.builder()
                                .brand(productProjection.getBrand())
                                .category(productProjection.getCategory())
                                .cost(productProjection.getCost())
                                .build()
                ).toList())
                .build();
    }

    @Override
    public LowestPriceResponse getLowestPrice() {
        LowestPriceProjection lowestPrice =
                categoryRepository
                        .getLowestPrice()
                        .stream().min(Comparator.comparing(LowestPriceProjection::getTotalCost))
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR, "Error calculating minimum cost"));

        Brand brand = brandRepository.getBrandById(lowestPrice.getBrandId());

        return LowestPriceResponse.builder()
                .lowestPrice(LowestPriceDto.builder()
                        .total(lowestPrice.getTotalCost())
                        .brand(brand.getBrandName())
                        .categories(brand.getCategories()
                                .stream()
                                .map(category -> CategoryDto.builder()
                                        .categoryName(category.getCategoryName())
                                        .cost(category.getCost())
                                        .build()).toList())
                        .build())
                .build();
    }

    @Override
    public PriceRangeResponse getPriceRange(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name cannot be empty");
        }

        List<Category> categories = categoryRepository.getCategoriesByCategoryName(category);

        if (categories.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        List<Long> costs = categories.stream().map(Category::getCost).toList();
        Long minCost = costs.stream()
                .min(Long::compare)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Error calculating minimum cost"));
        Long maxCost = costs.stream()
                .max(Long::compare)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Error calculating maximum cost"));

        List<Category> lowestPriceCategories = categories.stream()
                .filter(c -> Objects.equals(c.getCost(), minCost))
                .toList();

        List<Category> highestPriceCategories = categories.stream()
                .filter(c -> Objects.equals(c.getCost(), maxCost))
                .toList();

        return PriceRangeResponse.builder()
                .category(category)
                .lowestPrice(SampleMapper.fromCategoryListToProductList(lowestPriceCategories))
                .highestPrice(SampleMapper.fromCategoryListToProductList(highestPriceCategories))
                .build();
    }
}
