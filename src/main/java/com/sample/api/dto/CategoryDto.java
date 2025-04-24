package com.sample.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CategoryDto {
    private Long id;
    private String categoryName;
    private Long cost;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
