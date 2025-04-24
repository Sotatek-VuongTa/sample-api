package com.sample.api.dto;

import com.sample.api.entity.Brand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class AdminDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class CreateProductDto {

        String categoryName;
        Long cost;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class CreateRequestDto {
        Long topPrice;
        Long jacketPrice;
        Long sneakerPrice;
        Long bagPrice;
        Long hatPrice;
        Long sockPrice;
        Long accessoryPrice;

        String brand;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class UpdateRequestDto {
        String brand;
        String category;
        Long cost;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class DeleteRequestDto{
        String brand;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class CreateResponseDto{
        String message;
        BrandDto brand;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class UpdateCategoryDto {
        private String categoryId;
        private String categoryName;
        private Double cost;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class UpdateResponseDto {
        private String brandId;
        private String brandName;
        private List<CategoryResponseDto> categories;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class CategoryResponseDto {
        private String categoryId;
        private String categoryName;
        private Double cost;
    }

}
