package com.example.apidemo.demo.controllers;

import com.example.apidemo.demo.models.Product;
import com.example.apidemo.demo.models.ResponseObject;
import com.example.apidemo.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/Products")
public class ProductController {
    // dependency injection
    @Autowired
    private ProductRepository repository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllProducts(){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Ok","Get data successfully",repository.findAll())
        );
    }

    @GetMapping("/{id}")
        // http://localhost:8080/api/v1/Products/{id}
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<Product> foundProduct = repository.findById(id);
        if (foundProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Query product successfully", foundProduct)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Failed", "Cannot find product with id = " + id, "")
            );
        }
    }

    @PostMapping("/add-product")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product body) {
        List<Product> foundProduct = repository.findByProductName(body.getProductName().trim());
        if (foundProduct.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed", "This product is already exists", "")
            );
        } else
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Insert product successfully", repository.save(body))
            );
    }


    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product body, @PathVariable Long id) {
        Product product = repository.findById(id)
                .map(p -> {
                    List<Product> foundProduct = repository.findByProductName(body.getProductName());
                    if (foundProduct.size() > 0) {
                        if (foundProduct.get(0).getId() == id) {
                            p.setProductName(body.getProductName());
                            p.setPrice(body.getPrice());
                            p.setYear(body.getYear());
                            p.setUrl(body.getUrl());
                            return repository.save(p);
                        } else {
                            return null;
                        }

                    } else {
                        p.setProductName(body.getProductName());
                        p.setPrice(body.getPrice());
                        p.setYear(body.getYear());
                        p.setUrl(body.getUrl());
                        return repository.save(p);
                    }
                }).orElseGet(() -> {
                            return null;
                        }
                );
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Ok", "Product not found or already exists", "")
            );
        } else
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Update successfully", product)
            );
    }

    @DeleteMapping("/delete-product/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        boolean exists = repository.existsById(id);
        if (exists) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Delete successfully", "")
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Ok", "Product not found", "")
            );
        }
    }

}
