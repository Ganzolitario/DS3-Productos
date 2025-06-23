package com.example.spring_api.services;

import com.example.spring_api.Product;
import com.example.spring_api.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setIdProducto(1L);
        testProduct.setNombreProducto("Smartphone");
        testProduct.setDescripcion("Teléfono inteligente de última generación");
        testProduct.setPrecio(new BigDecimal("599.99"));
        testProduct.setStock(25);
        testProduct.setCategoria("Electrónicos");
    }

    @Test
    void testGetProducts_ShouldReturnAllProducts() {
        // Given
        List<Product> expectedProducts = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // When
        List<Product> actualProducts = productService.getProducts();

        // Then
        assertNotNull(actualProducts);
        assertEquals(1, actualProducts.size());
        assertEquals("Smartphone", actualProducts.get(0).getNombreProducto());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProduct_WithValidId_ShouldReturnProduct() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // When
        Product actualProduct = productService.getProduct(1L);

        // Then
        assertNotNull(actualProduct);
        assertEquals("Smartphone", actualProduct.getNombreProducto());
        assertEquals(1L, actualProduct.getIdProducto());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProduct_WithInvalidId_ShouldReturnNull() {
        // Given
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Product actualProduct = productService.getProduct(999L);

        // Then
        assertNull(actualProduct);
        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    void testAddProduct_ShouldSaveAndReturnProduct() {
        // Given
        Product newProduct = new Product();
        newProduct.setNombreProducto("Tablet");
        newProduct.setPrecio(new BigDecimal("299.99"));
        newProduct.setStock(15);

        when(productRepository.save(any(Product.class))).thenReturn(newProduct);

        // When
        Product savedProduct = productService.addProduct(newProduct);

        // Then
        assertNotNull(savedProduct);
        assertEquals("Tablet", savedProduct.getNombreProducto());
        assertEquals(new BigDecimal("299.99"), savedProduct.getPrecio());
        verify(productRepository, times(1)).save(newProduct);
    }

    @Test
    void testUpdateProduct_ShouldUpdateAndReturnProduct() {
        // Given
        Product updatedProduct = new Product();
        updatedProduct.setIdProducto(1L);
        updatedProduct.setNombreProducto("Smartphone Actualizado");
        updatedProduct.setPrecio(new BigDecimal("649.99"));
        updatedProduct.setStock(30);

        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // When
        Product result = productService.updateProduct(updatedProduct);

        // Then
        assertNotNull(result);
        assertEquals("Smartphone Actualizado", result.getNombreProducto());
        assertEquals(new BigDecimal("649.99"), result.getPrecio());
        assertEquals(30, result.getStock());
        verify(productRepository, times(1)).save(updatedProduct);
    }

    @Test
    void testDeleteProduct_ShouldCallRepositoryDelete() {
        // Given
        Long productId = 1L;
        doNothing().when(productRepository).deleteById(productId);

        // When
        productService.deleteProduct(productId);

        // Then
        verify(productRepository, times(1)).deleteById(productId);
    }
} 