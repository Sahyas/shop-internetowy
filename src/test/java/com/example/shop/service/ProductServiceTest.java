package com.example.shop.service;

import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private ProductService productService;
    
    private Product testProduct;
    
    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId("1");
        testProduct.setName("Test Product");
        testProduct.setDescription("Test description");
        testProduct.setPrice(99.99);
        testProduct.setCategory("Elektronika");
        testProduct.setStockQuantity(10);
    }
    
    @Test
    void findAll_shouldReturnAllProducts() {
        // given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(products);
        
        // when
        List<Product> result = productService.findAll();
        
        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }
    
    @Test
    void findById_whenProductExists_shouldReturnProduct() {
        // given
        when(productRepository.findById("1")).thenReturn(Optional.of(testProduct));
        
        // when
        Optional<Product> result = productService.findById("1");
        
        // then
        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getName());
        verify(productRepository, times(1)).findById("1");
    }
    
    @Test
    void findById_whenProductDoesNotExist_shouldReturnEmpty() {
        // given
        when(productRepository.findById("999")).thenReturn(Optional.empty());
        
        // when
        Optional<Product> result = productService.findById("999");
        
        // then
        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById("999");
    }
    
    @Test
    void save_shouldSaveProduct() {
        // given
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        
        // when
        Product result = productService.save(testProduct);
        
        // then
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals(99.99, result.getPrice());
        verify(productRepository, times(1)).save(testProduct);
    }
    
    @Test
    void delete_shouldDeleteProduct() {
        // given
        doNothing().when(productRepository).delete("1");
        
        // when
        productService.delete("1");
        
        // then
        verify(productRepository, times(1)).delete("1");
    }
    
    @Test
    void findByCategory_shouldReturnFilteredProducts() {
        // given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByCategory("Elektronika")).thenReturn(products);
        
        // when
        List<Product> result = productService.findByCategory("Elektronika");
        
        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Elektronika", result.get(0).getCategory());
        verify(productRepository, times(1)).findByCategory("Elektronika");
    }
}
