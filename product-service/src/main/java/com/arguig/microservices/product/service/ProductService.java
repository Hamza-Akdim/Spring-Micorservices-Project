package com.arguig.microservices.product.service;

import com.arguig.microservices.product.dto.ProductRequest;
import com.arguig.microservices.product.dto.ProductResponse;
import com.arguig.microservices.product.model.Product;
import com.arguig.microservices.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // to create the log statement
public class ProductService {

    private final ProductRepository productRepository;

    // Create Product
    public ProductResponse createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();
        productRepository.save(product);
        log.info("product created successfully");
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }

    // Get All Products
    public List<ProductResponse> getAllProducts(){
        return productRepository.findAll().stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice()))
                .toList();
    }

    // Delete Product By ID
    public ResponseEntity<String> deleteProductById(String id) {
        if (!productRepository.existsById(id)) {
            log.warn("Product with ID {} does not exist", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with ID " + id + " does not exist.");
        }

        productRepository.deleteById(id);
        log.info("Product with ID {} deleted successfully", id);
        return ResponseEntity.ok("Product with ID " + id + " deleted successfully.");
    }

    // Update Product By ID
    public ResponseEntity<ProductResponse> updateProductById(String id, ProductRequest productRequest) {
        if (!productRepository.existsById(id)) {
            log.warn("Product with ID {} does not exist", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with ID " + id + " does not exist.");
        }

        Product existingProduct = productRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with ID " + id + " does not exist.")
        );

        // Update fields
        existingProduct.setName(productRequest.name());
        existingProduct.setDescription(productRequest.description());
        existingProduct.setPrice(productRequest.price());

        // Save updated product
        productRepository.save(existingProduct);

        log.info("Product with ID {} updated successfully", id);

        ProductResponse updatedProductResponse = new ProductResponse(
                existingProduct.getId(),
                existingProduct.getName(),
                existingProduct.getDescription(),
                existingProduct.getPrice()
        );
        return ResponseEntity.ok(updatedProductResponse);
    }

}
