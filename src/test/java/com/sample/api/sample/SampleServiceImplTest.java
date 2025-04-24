package com.sample.api.sample;

import com.sample.api.dto.SampleDto.CheapestComboResponse;
import com.sample.api.dto.SampleDto.LowestPriceResponse;
import com.sample.api.dto.SampleDto.PriceRangeResponse;
import com.sample.api.entity.Brand;
import com.sample.api.entity.Category;
import com.sample.api.projection.CheapestComboProjection;
import com.sample.api.projection.LowestPriceProjection;
import com.sample.api.repository.BrandRepository;
import com.sample.api.repository.CategoryRepository;
import com.sample.api.service.impl.SampleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SampleServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private SampleServiceImpl sampleService;

    @Test
    void getCheapestCombo_Success() {
        // Arrange
        List<CheapestComboProjection> mockProducts = List.of(
                new CheapestComboProjection() {
                    public String getBrand() { return "Brand1"; }
                    public String getCategory() { return "Category1"; }
                    public Long getCost() { return 100L; }
                },
                new CheapestComboProjection() {
                    public String getBrand() { return "Brand2"; }
                    public String getCategory() { return "Category2"; }
                    public Long getCost() { return 200L; }
                }
        );
        when(categoryRepository.findCheapestCombo()).thenReturn(mockProducts);

        // Act
        CheapestComboResponse response = sampleService.getCheapestCombo();

        // Assert
        assertNotNull(response);
        assertEquals(300L, response.getTotalCost());
        assertEquals(2, response.getProducts().size());
        verify(categoryRepository, times(1)).findCheapestCombo();
    }

    @Test
    void getLowestPrice_Success() {
        // Arrange
        LowestPriceProjection mockLowestPrice = new LowestPriceProjection() {
            public Long getBrandId() { return 1L; }
            public Long getTotalCost() { return 300L; }
        };
        Brand mockBrand = Brand.builder()
                .id(1L)
                .brandName("TestBrand")
                .categories(new ArrayList<>())
                .build();

        when(categoryRepository.getLowestPrice()).thenReturn(List.of(mockLowestPrice));
        when(brandRepository.getBrandById(1L)).thenReturn(mockBrand);

        // Act
        LowestPriceResponse response = sampleService.getLowestPrice();

        // Assert
        assertNotNull(response);
        assertEquals("TestBrand", response.getLowestPrice().getBrand());
        assertEquals(300L, response.getLowestPrice().getTotal());
        verify(categoryRepository, times(1)).getLowestPrice();
        verify(brandRepository, times(1)).getBrandById(1L);
    }

    @Test
    void getPriceRange_Success() {
        // Arrange
        String categoryName = "TestCategory";
        Brand brand = Brand.builder().brandName("TestBrand").build();
        List<Category> categories = List.of(
                Category.builder().categoryName(categoryName).cost(100L).brand(brand).build(),
                Category.builder().categoryName(categoryName).cost(200L).brand(brand).build()
        );

        when(categoryRepository.getCategoriesByCategoryName(categoryName)).thenReturn(categories);

        // Act
        PriceRangeResponse response = sampleService.getPriceRange(categoryName);

        // Assert
        assertNotNull(response);
        assertEquals(categoryName, response.getCategory());
        assertEquals(100L, response.getLowestPrice().get(0).getCost());
        assertEquals(200L, response.getHighestPrice().get(0).getCost());
    }

    @Test
    void getPriceRange_EmptyCategory_ThrowsException() {
        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> sampleService.getPriceRange(""));
    }

    @Test
    void getPriceRange_CategoryNotFound_ThrowsException() {
        // Arrange
        when(categoryRepository.getCategoriesByCategoryName(anyString()))
                .thenReturn(List.of());

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> sampleService.getPriceRange("NonExistentCategory"));
    }
}