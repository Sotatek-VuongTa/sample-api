package com.sample.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BrandDto {
    private Long id;
    private String brandName;
    private List<CategoryDto> categories;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
