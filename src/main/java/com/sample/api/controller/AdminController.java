package com.sample.api.controller;

import com.sample.api.dto.AdminDto.*;
import com.sample.api.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin API", description = "Operations for managing brands and categories")
public class AdminController {

    private final AdminService adminService;

    @Operation(
            summary = "Create new brand",
            description = "Creates a new brand with its categories and their respective costs"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Brand successfully created",
            content = @Content(schema = @Schema(implementation = CreateResponseDto.class))
    )
    @PutMapping("create")
    public ResponseEntity<CreateResponseDto> createBrand(@RequestBody CreateRequestDto request) {
        return ResponseEntity.ok(adminService.create(request));
    }

    @Operation(
            summary = "Update brand category",
            description = "Update brand category information including cost"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully updated",
            content = @Content(schema = @Schema(implementation = UpdateResponseDto.class))
    )
    @PostMapping("update")
    public ResponseEntity<UpdateResponseDto> updateBrand(@RequestBody UpdateRequestDto request) {
        return ResponseEntity.ok(adminService.update(request));
    }

    @ApiResponse(
            responseCode = "200",
            description = "Brand successfully deleted",
            content = @Content(schema = @Schema(type = "string", example = "Successfully deleted brand: BrandName"))
    )
    @DeleteMapping("delete")
    public ResponseEntity<String> deleteBrand(@RequestBody DeleteRequestDto request) {
        return ResponseEntity.ok(adminService.delete(request));
    }
}
