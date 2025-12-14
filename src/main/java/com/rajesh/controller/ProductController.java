package com.rajesh.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rajesh.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    // Define logger
    private static final Logger logger = LogManager.getLogger(ProductController.class);

    // In-memory list to store products
    private List<Product> productList = new ArrayList<>();

    // API 1: GET - Retrieve all products
    @GetMapping
    public List<Product> getAllProducts() {
        logger.info("Returning all products with " + Thread.currentThread().getStackTrace()[1].getMethodName());
        return productList;
    }

    // API 2: GET - Retrieve a product by ID (using a path variable)
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        logger.info("Returning product with id " + Integer.toString(id) + " by using method " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Optional<Product> product = productList.stream().filter(p -> p.getId() == id).findFirst();
        return product.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // API 3: POST - Create a new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product newProduct) {
        if (newProduct.getName() == null || newProduct.getPrice() < 0) {
            return ResponseEntity.badRequest().build();
        }
        newProduct.setId(productList.size() > 0 ? productList.get(productList.size() - 1).getId() + 1 : 1); // Improved ID generation
        try{
            logger.info("Adding new product with id " + newProduct.getId() + " by using method " + Thread.currentThread().getStackTrace()[1].getMethodName());
            productList.add(newProduct);
        }
        catch (Exception e) {
            logger.error("Adding new product with id " + newProduct.getId() + " is failed by using method " + Thread.currentThread().getStackTrace()[1].getMethodName());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    // API 4: PUT - Update an existing product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product updatedProduct) {
        Optional<Product> existingProduct = productList.stream().filter(p -> p.getId() == id).findFirst();
        if (updatedProduct.getName() == null || updatedProduct.getPrice() < 0) {
            return ResponseEntity.badRequest().build();
        }

        if (existingProduct.isPresent()) {
            logger.info("Updating existing product with id " + Integer.toString(id) + " by using method " + Thread.currentThread().getStackTrace()[1].getMethodName());
            Product product = existingProduct.get();
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            return ResponseEntity.ok(product);
        } else {
            logger.error("Adding new product with id " + Integer.toString(id) + " is not found in the Data base");
            return ResponseEntity.notFound().build();
        }
    }

    // API 5: DELETE - Delete a product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        boolean removed = productList.removeIf(p -> p.getId() == id);
        if (removed) {
            logger.info("Deleting existing product with id " + Integer.toString(id) + " by using method " + Thread.currentThread().getStackTrace()[1].getMethodName());
            return ResponseEntity.noContent().build();
        } else {
            logger.error("Deleting existing product with id " + Integer.toString(id) + " is not found in the Data base");
            return ResponseEntity.notFound().build();
        }
    }
}
