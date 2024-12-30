package com.arguig.microservices.product.controller;

import com.arguig.microservices.product.dto.ProductRequest;
import com.arguig.microservices.product.dto.ProductResponse;
import com.arguig.microservices.product.model.Product;
import com.arguig.microservices.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    // Create New Product
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest){
        return productService.createProduct(productRequest);
    }

    // Get All Products
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }

    // Delete Product By ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteProductById(@PathVariable("id") String id) {
        log.info("Deleting product with ID: {}", id);
        return productService.deleteProductById(id);
    }

    // Update Product By ID
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProductById(
            @PathVariable("id") String id,
            @RequestBody ProductRequest productRequest) {
        log.info("Updating product with ID: {}", id);
        return productService.updateProductById(id, productRequest);
    }
}