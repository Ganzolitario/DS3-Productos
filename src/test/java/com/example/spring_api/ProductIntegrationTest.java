package com.example.spring_api;

import com.example.spring_api.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductIntegrationTest {

    @Autowired
    private ProductService productService;

    @Test
    void testCompleteProductLifecycle_ShouldWorkEndToEnd() {
        // Given - Crear un producto
        Product newProduct = new Product();
        newProduct.setNombreProducto("Teclado Mecánico");
        newProduct.setDescripcion("Teclado mecánico RGB con switches Cherry MX");
        newProduct.setPrecio(new BigDecimal("149.99"));
        newProduct.setStock(20);
        newProduct.setCategoria("Periféricos");

        // When - Guardar el producto
        Product savedProduct = productService.addProduct(newProduct);

        // Then - Verificar que se guardó correctamente
        assertNotNull(savedProduct);
        assertNotNull(savedProduct.getIdProducto());
        assertEquals("Teclado Mecánico", savedProduct.getNombreProducto());
        assertEquals(new BigDecimal("149.99"), savedProduct.getPrecio());

        // When - Buscar el producto por ID
        Product foundProduct = productService.getProduct(savedProduct.getIdProducto());

        // Then - Verificar que se encontró
        assertNotNull(foundProduct);
        assertEquals(savedProduct.getIdProducto(), foundProduct.getIdProducto());
        assertEquals("Teclado Mecánico", foundProduct.getNombreProducto());

        // When - Actualizar el producto
        foundProduct.setPrecio(new BigDecimal("129.99"));
        foundProduct.setStock(15);
        Product updatedProduct = productService.updateProduct(foundProduct);

        // Then - Verificar que se actualizó
        assertNotNull(updatedProduct);
        assertEquals(new BigDecimal("129.99"), updatedProduct.getPrecio());
        assertEquals(15, updatedProduct.getStock());

        // When - Obtener todos los productos
        List<Product> allProducts = productService.getProducts();

        // Then - Verificar que está en la lista
        assertFalse(allProducts.isEmpty());
        assertTrue(allProducts.stream()
                .anyMatch(p -> p.getIdProducto().equals(savedProduct.getIdProducto())));

        // When - Eliminar el producto
        productService.deleteProduct(savedProduct.getIdProducto());

        // Then - Verificar que se eliminó
        Product deletedProduct = productService.getProduct(savedProduct.getIdProducto());
        assertNull(deletedProduct);
    }

    @Test
    void testMultipleProducts_ShouldHandleCorrectly() {
        // Given - Crear múltiples productos
        Product product1 = createTestProduct("Mouse Gaming", "Mouse para gaming", new BigDecimal("59.99"), 30);
        Product product2 = createTestProduct("Monitor 27\"", "Monitor Full HD", new BigDecimal("199.99"), 10);
        Product product3 = createTestProduct("Webcam HD", "Webcam 1080p", new BigDecimal("79.99"), 25);

        // When - Guardar todos los productos
        Product saved1 = productService.addProduct(product1);
        Product saved2 = productService.addProduct(product2);
        Product saved3 = productService.addProduct(product3);

        // Then - Verificar que todos se guardaron
        assertNotNull(saved1.getIdProducto());
        assertNotNull(saved2.getIdProducto());
        assertNotNull(saved3.getIdProducto());

        // When - Obtener todos los productos
        List<Product> allProducts = productService.getProducts();

        // Then - Verificar que están todos en la lista
        assertEquals(3, allProducts.size());
        assertTrue(allProducts.stream().anyMatch(p -> p.getNombreProducto().equals("Mouse Gaming")));
        assertTrue(allProducts.stream().anyMatch(p -> p.getNombreProducto().equals("Monitor 27\"")));
        assertTrue(allProducts.stream().anyMatch(p -> p.getNombreProducto().equals("Webcam HD")));
    }

    @Test
    void testProductWithSpecialCharacters_ShouldHandleCorrectly() {
        // Given - Producto con caracteres especiales
        Product specialProduct = new Product();
        specialProduct.setNombreProducto("Café & Té Premium");
        specialProduct.setDescripcion("Café orgánico 100% colombiano con notas de vainilla");
        specialProduct.setPrecio(new BigDecimal("24.99"));
        specialProduct.setStock(100);
        specialProduct.setCategoria("Bebidas");

        // When - Guardar el producto
        Product savedProduct = productService.addProduct(specialProduct);

        // Then - Verificar que se guardó correctamente
        assertNotNull(savedProduct);
        assertEquals("Café & Té Premium", savedProduct.getNombreProducto());
        assertEquals("Café orgánico 100% colombiano con notas de vainilla", savedProduct.getDescripcion());
    }

    private Product createTestProduct(String nombre, String descripcion, BigDecimal precio, Integer stock) {
        Product product = new Product();
        product.setNombreProducto(nombre);
        product.setDescripcion(descripcion);
        product.setPrecio(precio);
        product.setStock(stock);
        product.setCategoria("Electrónicos");
        return product;
    }
} 