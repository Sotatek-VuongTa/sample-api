package com.sample.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class SampleDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class CheapestComboResponse {
        private List<ProductDto> products;
        private Long totalCost;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class LowestPriceResponse {
        LowestPriceDto lowestPrice;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class LowestPriceDto {
        String brand;
        List<CategoryDto> categories;
        Long total;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class PriceRangeResponse {
        String category;
        List<ProductDto> lowestPrice;
        List<ProductDto> highestPrice;
    }
}
