package com.example.spring_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
    }

    @Test
    void testProductCreation_WithValidData_ShouldSetAllFields() {
        // Given
        Long id = 1L;
        String nombre = "Auriculares Bluetooth";
        String descripcion = "Auriculares inalámbricos con cancelación de ruido";
        BigDecimal precio = new BigDecimal("89.99");
        Integer stock = 50;
        String categoria = "Audio";
        Timestamp fechaCreacion = new Timestamp(System.currentTimeMillis());

        // When
        product.setIdProducto(id);
        product.setNombreProducto(nombre);
        product.setDescripcion(descripcion);
        product.setPrecio(precio);
        product.setStock(stock);
        product.setCategoria(categoria);
        product.setFechaCreacion(fechaCreacion);

        // Then
        assertEquals(id, product.getIdProducto());
        assertEquals(nombre, product.getNombreProducto());
        assertEquals(descripcion, product.getDescripcion());
        assertEquals(precio, product.getPrecio());
        assertEquals(stock, product.getStock());
        assertEquals(categoria, product.getCategoria());
        assertEquals(fechaCreacion, product.getFechaCreacion());
    }

    @Test
    void testProductEquality_WithSameId_ShouldBeEqual() {
        // Given
        Product product1 = new Product();
        product1.setIdProducto(1L);
        product1.setNombreProducto("Producto 1");

        Product product2 = new Product();
        product2.setIdProducto(1L);
        product2.setNombreProducto("Producto 1");

        // When & Then
        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    void testProductEquality_WithDifferentId_ShouldNotBeEqual() {
        // Given
        Product product1 = new Product();
        product1.setIdProducto(1L);
        product1.setNombreProducto("Producto 1");

        Product product2 = new Product();
        product2.setIdProducto(2L);
        product2.setNombreProducto("Producto 1");

        // When & Then
        assertNotEquals(product1, product2);
        assertNotEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    void testProductToString_ShouldContainProductInformation() {
        // Given
        product.setIdProducto(1L);
        product.setNombreProducto("Monitor 4K");
        product.setPrecio(new BigDecimal("299.99"));

        // When
        String toString = product.toString();

        // Then
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Monitor 4K"));
        assertTrue(toString.contains("299.99"));
    }

    @Test
    void testProductWithNullValues_ShouldHandleGracefully() {
        // Given & When
        product.setIdProducto(null);
        product.setNombreProducto(null);
        product.setDescripcion(null);
        product.setPrecio(null);
        product.setStock(null);
        product.setCategoria(null);
        product.setFechaCreacion(null);

        // Then
        assertNull(product.getIdProducto());
        assertNull(product.getNombreProducto());
        assertNull(product.getDescripcion());
        assertNull(product.getPrecio());
        assertNull(product.getStock());
        assertNull(product.getCategoria());
        assertNull(product.getFechaCreacion());
    }

    @Test
    void testProductWithZeroValues_ShouldSetCorrectly() {
        // Given
        BigDecimal precioCero = BigDecimal.ZERO;
        Integer stockCero = 0;

        // When
        product.setPrecio(precioCero);
        product.setStock(stockCero);

        // Then
        assertEquals(BigDecimal.ZERO, product.getPrecio());
        assertEquals(0, product.getStock());
    }

    @Test
    void testProductWithNegativeStock_ShouldSetCorrectly() {
        // Given
        Integer stockNegativo = -5;

        // When
        product.setStock(stockNegativo);

        // Then
        assertEquals(-5, product.getStock());
    }
} 