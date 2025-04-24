package com.sample.api.admin;

import com.sample.api.dto.AdminDto.*;
import com.sample.api.entity.Brand;
import com.sample.api.entity.Category;
import com.sample.api.repository.BrandRepository;
import com.sample.api.repository.CategoryRepository;
import com.sample.api.service.impl.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void create_Success() {
        // Arrange
        CreateRequestDto request = CreateRequestDto.builder()
                .brand("NewBrand")
                .topPrice(100L)
                .jacketPrice(200L)
                .sneakerPrice(300L)
                .bagPrice(150L)
                .hatPrice(50L)
                .sockPrice(20L)
                .accessoryPrice(30L)
                .build();

        Brand savedBrand = Brand.builder()
                .brandName("NewBrand")
                .categories(List.of(
                        Category.builder()
                                .categoryName("Tops")
                                .cost(100L)
                                .build(),
                        Category.builder()
                                .categoryName("Jackets")
                                .cost(200L)
                                .build()
                        // ... other categories
                ))
                .build();

        when(brandRepository.findBandByBrandName(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.findMaxPOder()).thenReturn(0L);
        when(brandRepository.save(any(Brand.class))).thenReturn(savedBrand);

        // Act
        CreateResponseDto response = adminService.create(request);

        // Assert
        assertNotNull(response);
        assertEquals("created brand successfully", response.getMessage());
        assertNotNull(response.getBrand());
        assertEquals("NewBrand", response.getBrand().getBrandName());
        assertNotNull(response.getBrand().getCategories());
        verify(brandRepository).save(any(Brand.class));
    }

    @Test
    void create_InvalidCost_ThrowsException() {
        // Arrange
        CreateRequestDto request = CreateRequestDto.builder()
                .brand("NewBrand")
                .topPrice(-100L)  // Invalid cost
                .build();

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> adminService.create(request));
    }

    @Test
    void create_BrandExists_ThrowsException() {
        // Arrange
        CreateRequestDto request = CreateRequestDto.builder()
                .brand("A")
                .topPrice(100L)
                .jacketPrice(200L)
                .sneakerPrice(300L)
                .bagPrice(150L)
                .hatPrice(50L)
                .sockPrice(20L)
                .accessoryPrice(30L)
                .build();

        when(brandRepository.findBandByBrandName("A"))
                .thenReturn(Optional.of(Brand.builder().build()));

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> adminService.create(request));
        verify(brandRepository, never()).save(any());

    }

    @Test
    void update_Success() {
        // Arrange
        UpdateRequestDto request = UpdateRequestDto.builder()
                .brand("TestBrand")
                .category("Tops")
                .cost(150L)
                .build();

        Category category = Category.builder()
                .id(1L)
                .categoryName("Tops")
                .cost(100L)
                .build();

        Brand existingBrand = Brand.builder()
                .id(1L)
                .brandName("TestBrand")
                .categories(new ArrayList<>(List.of(category)))
                .build();

        when(brandRepository.findBandByBrandName("TestBrand"))
                .thenReturn(Optional.of(existingBrand));
        when(brandRepository.save(any(Brand.class))).thenReturn(existingBrand);

        // Act
        UpdateResponseDto response = adminService.update(request);

        // Assert
        assertNotNull(response);
        assertEquals("1", response.getBrandId());
        assertEquals("TestBrand", response.getBrandName());
        assertFalse(response.getCategories().isEmpty());
        assertEquals(150.0, response.getCategories().get(0).getCost());
        verify(brandRepository).save(any(Brand.class));
    }

    @Test
    void update_BrandNotFound_ThrowsException() {
        // Arrange
        UpdateRequestDto request = UpdateRequestDto.builder()
                .brand("NonExistentBrand")
                .category("Tops")
                .cost(150L)
                .build();

        when(brandRepository.findBandByBrandName(anyString()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> adminService.update(request));
    }

    @Test
    void update_CategoryNotFound_ThrowsException() {
        // Arrange
        UpdateRequestDto request = UpdateRequestDto.builder()
                .brand("TestBrand")
                .category("NonExistentCategory")
                .cost(150L)
                .build();

        Brand brand = Brand.builder()
                .brandName("TestBrand")
                .categories(new ArrayList<>())
                .build();

        when(brandRepository.findBandByBrandName("TestBrand"))
                .thenReturn(Optional.of(brand));

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> adminService.update(request));
    }

    @Test
    void delete_Success() {
        // Arrange
        DeleteRequestDto request = DeleteRequestDto.builder()
                .brand("BrandToDelete")
                .build();

        Brand brandToDelete = Brand.builder()
                .id(1L)
                .brandName("BrandToDelete")
                .build();

        when(brandRepository.findBandByBrandName("BrandToDelete"))
                .thenReturn(Optional.of(brandToDelete));

        // Act
        String response = adminService.delete(request);

        // Assert
        assertNotNull(response);
        assertEquals("Successfully deleted brand: BrandToDelete", response);
        verify(brandRepository).delete(brandToDelete);
    }

    @Test
    void delete_EmptyBrandName_ThrowsException() {
        // Arrange
        DeleteRequestDto request = DeleteRequestDto.builder()
                .brand("")
                .build();

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> adminService.delete(request));
        verify(brandRepository, never()).delete(any());
    }

    @Test
    void delete_BrandNotFound_ThrowsException() {
        // Arrange
        DeleteRequestDto request = DeleteRequestDto.builder()
                .brand("NonExistentBrand")
                .build();

        when(brandRepository.findBandByBrandName(anyString()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> adminService.delete(request));
        verify(brandRepository, never()).delete(any());
    }
}
