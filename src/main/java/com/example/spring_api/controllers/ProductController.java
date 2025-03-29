package com.example.spring_api.controllers;

import com.example.spring_api.Product;
import com.example.spring_api.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    @CrossOrigin
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable ("id") Long id) {
        return productService.getProduct(id);
    }

    @PutMapping("/product/{id}")
    @CrossOrigin
    public Product updateProduct(@RequestBody() Product product, @PathVariable Long id) {
        return productService.updateProduct(product);
    }

    @PostMapping("/products")
    @CrossOrigin
    public ResponseEntity<Product> addNew(@RequestBody() Product product) {
        Product newProduct = productService.addProduct(product);
        return  ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @DeleteMapping("/product/{id}")
    @CrossOrigin
    public void deleteProduct(@PathVariable ("id") Long id) {
        productService.deleteProduct(id);
    }


}
