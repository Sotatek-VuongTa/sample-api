package com.sample.api.service.impl;

import com.sample.api.dto.AdminDto.*;
import com.sample.api.dto.ProductDto;
import com.sample.api.entity.Brand;
import com.sample.api.entity.Category;
import com.sample.api.mapper.AdminMapper;
import com.sample.api.repository.BrandRepository;
import com.sample.api.repository.CategoryRepository;
import com.sample.api.service.AdminService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    @Override
    @Transactional
    public CreateResponseDto create(CreateRequestDto request) {
        List<ProductDto> products = AdminMapper.mapToProductDtoList(request);

        products.forEach(product -> {
            if (product.getCost() <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cost for category '" + product.getCategory() + "' must be greater than 0");
            }
        });

        brandRepository.findBandByBrandName(request.getBrand()).ifPresent(
                existing -> {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Brand already exists: " + request.getBrand());
                }
        );

        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        Long pOder = categoryRepository.findMaxPOder();

        Brand newBrand = Brand.builder()
                .brandName(request.getBrand())
                .categories(
                        products.stream()
                                .map(product -> Category.builder()
                                        .categoryName(product.getCategory())
                                        .pOrder(pOder + 1)
                                        .createdAt(currentTime)
                                        .updatedAt(currentTime)
                                        .cost(product.getCost())
                                        .build()).toList())
                .createdAt(currentTime)
                .updatedAt(currentTime)
                .build();

        return AdminMapper.fromBrandToCreateResponse(brandRepository.save(newBrand));
    }

    @Override
    @Transactional
    public UpdateResponseDto update(UpdateRequestDto request) {

        if (request.getBrand() == null || request.getBrand().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Brand name cannot be empty");
        }

        if (request.getCategory() == null || request.getCategory().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name cannot be empty");
        }

        if (request.getCost() == null || request.getCost() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cost must be greater than 0");
        }

        Brand existingBrand = brandRepository.findBandByBrandName(request.getBrand())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Brand not found"));

        Category categoryToUpdate = existingBrand.getCategories().stream()
                .filter(c -> c.getCategoryName().equals(request.getCategory()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Category not found: " + request.getCategory()));

        categoryToUpdate.setCost(request.getCost());

        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        existingBrand.setUpdatedAt(currentTime);
        categoryToUpdate.setUpdatedAt(currentTime);

        Brand updatedBrand = brandRepository.save(existingBrand);

        return UpdateResponseDto.builder()
                .brandId(updatedBrand.getId().toString())
                .brandName(updatedBrand.getBrandName())
                .categories(updatedBrand.getCategories().stream()
                        .map(category -> CategoryResponseDto.builder()
                                .categoryId(category.getId().toString())
                                .categoryName(category.getCategoryName())
                                .cost(category.getCost().doubleValue())
                                .build())
                        .toList())
                .build();

    }


    @Transactional
    public String delete(DeleteRequestDto request) {
        if (request.getBrand() == null || request.getBrand().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Brand name cannot be empty");
        }

        Brand brandToDelete = brandRepository.findBandByBrandName(request.getBrand())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Brand not found"));

        brandRepository.delete(brandToDelete);

        return "Successfully deleted brand: " + request.getBrand();
    }

}
