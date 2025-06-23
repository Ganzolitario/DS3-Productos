package com.example.spring_api.controllers;

import com.example.spring_api.Product;
import com.example.spring_api.dto.TokenValidationResponse;
import com.example.spring_api.services.ProductService;
import com.example.spring_api.service.TokenValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private ProductService productService;
    
    @Autowired
    private TokenValidationService tokenValidationService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable ("id") Long id) {
        return productService.getProduct(id);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody() Product product, 
                                         @PathVariable Long id,
                                         @RequestHeader(value = "Authorization", required = false) String authorization) {
        
        TokenValidationResponse validation = tokenValidationService.validateAdminToken(authorization);
        
        if (!validation.isValid() || !validation.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Only administrators can update products. " + validation.getMessage());
        }
        
        Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @PostMapping("/products")
    public ResponseEntity<?> addNew(@RequestBody() Product product,
                                   @RequestHeader(value = "Authorization", required = false) String authorization) {
        
        TokenValidationResponse validation = tokenValidationService.validateAdminToken(authorization);
        
        if (!validation.isValid() || !validation.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Only administrators can add products. " + validation.getMessage());
        }
        
        Product newProduct = productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable ("id") Long id,
                                         @RequestHeader(value = "Authorization", required = false) String authorization) {
        
        TokenValidationResponse validation = tokenValidationService.validateAdminToken(authorization);
        
        if (!validation.isValid() || !validation.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Only administrators can delete products. " + validation.getMessage());
        }
        
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
