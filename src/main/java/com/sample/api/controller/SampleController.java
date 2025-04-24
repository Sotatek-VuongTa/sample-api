package com.sample.api.controller;

import com.sample.api.dto.SampleDto;
import com.sample.api.dto.SampleDto.LowestPriceResponse;
import com.sample.api.dto.SampleDto.CheapestComboResponse;
import com.sample.api.service.SampleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@Tag(name = "Sample API", description = "Operations for retrieving product pricing information")
public class SampleController {

    private final SampleService sampleService;

    @Operation(
            summary = "Get cheapest combination of products",
            description = "Retrieves the cheapest possible combination of products across all categories"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved cheapest combo",
            content = @Content(schema = @Schema(implementation = CheapestComboResponse.class))
    )
    @GetMapping("outfit/cheapest-combo")
    public ResponseEntity<CheapestComboResponse> cheapestCombo() {
        return ResponseEntity.ok(sampleService.getCheapestCombo());
    }

    @Operation(
            summary = "Get lowest price by brand",
            description = "Retrieves the brand with the lowest total price across all categories"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved lowest price",
            content = @Content(schema = @Schema(implementation = LowestPriceResponse.class))
    )
    @GetMapping("brands/lowest-price")
    public ResponseEntity<LowestPriceResponse> lowestPrice() {
        return ResponseEntity.ok(sampleService.getLowestPrice());
    }

    @Operation(
            summary = "Get price range for category",
            description = "Retrieves the lowest and highest priced products within a specific category"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved price range",
            content = @Content(schema = @Schema(implementation = SampleDto.PriceRangeResponse.class))
    )
    @GetMapping("categories/{category}/price-range")
    public ResponseEntity<SampleDto.PriceRangeResponse> priceRange(@RequestParam("category") String category) {
        return ResponseEntity.ok(sampleService.getPriceRange(category));
    }

}
