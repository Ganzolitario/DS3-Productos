package com.example.spring_api.controllers;

import com.example.spring_api.Product;
import com.example.spring_api.dto.TokenValidationResponse;
import com.example.spring_api.services.ProductService;
import com.example.spring_api.service.TokenValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private TokenValidationService tokenValidationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;
    private TokenValidationResponse validAdminResponse;
    private TokenValidationResponse invalidResponse;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setIdProducto(1L);
        testProduct.setNombreProducto("Laptop Gaming");
        testProduct.setDescripcion("Laptop para gaming de alta performance");
        testProduct.setPrecio(new BigDecimal("1299.99"));
        testProduct.setStock(10);
        testProduct.setCategoria("Electrónicos");

        validAdminResponse = new TokenValidationResponse();
        validAdminResponse.setValid(true);
        validAdminResponse.setAdmin(true);
        validAdminResponse.setMessage("Valid admin token");

        invalidResponse = new TokenValidationResponse();
        invalidResponse.setValid(false);
        invalidResponse.setAdmin(false);
        invalidResponse.setMessage("Invalid token");
    }

    @Test
    void testGetProducts_ShouldReturnAllProducts() throws Exception {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productService.getProducts()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreProducto").value("Laptop Gaming"))
                .andExpect(jsonPath("$[0].precio").value(1299.99));
    }

    @Test
    void testGetProduct_ShouldReturnProductById() throws Exception {
        // Given
        when(productService.getProduct(1L)).thenReturn(testProduct);

        // When & Then
        mockMvc.perform(get("/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProducto").value("Laptop Gaming"))
                .andExpect(jsonPath("$.idProducto").value(1));
    }

    @Test
    void testAddProduct_WithValidAdminToken_ShouldCreateProduct() throws Exception {
        // Given
        when(tokenValidationService.validateAdminToken(any())).thenReturn(validAdminResponse);
        when(productService.addProduct(any(Product.class))).thenReturn(testProduct);

        // When & Then
        mockMvc.perform(post("/products")
                .header("Authorization", "Bearer valid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreProducto").value("Laptop Gaming"));
    }

    @Test
    void testAddProduct_WithoutAdminToken_ShouldReturnForbidden() throws Exception {
        // Given
        when(tokenValidationService.validateAdminToken(any())).thenReturn(invalidResponse);

        // When & Then
        mockMvc.perform(post("/products")
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateProduct_WithValidAdminToken_ShouldUpdateProduct() throws Exception {
        // Given
        when(tokenValidationService.validateAdminToken(any())).thenReturn(validAdminResponse);
        when(productService.updateProduct(any(Product.class))).thenReturn(testProduct);

        // When & Then
        mockMvc.perform(put("/product/1")
                .header("Authorization", "Bearer valid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProducto").value("Laptop Gaming"));
    }

    @Test
    void testDeleteProduct_WithValidAdminToken_ShouldDeleteProduct() throws Exception {
        // Given
        when(tokenValidationService.validateAdminToken(any())).thenReturn(validAdminResponse);

        // When & Then
        mockMvc.perform(delete("/product/1")
                .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteProduct_WithoutAdminToken_ShouldReturnForbidden() throws Exception {
        // Given
        when(tokenValidationService.validateAdminToken(any())).thenReturn(invalidResponse);

        // When & Then
        mockMvc.perform(delete("/product/1")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isForbidden());
    }
} 