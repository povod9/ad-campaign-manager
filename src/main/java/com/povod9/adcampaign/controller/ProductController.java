package com.povod9.adcampaign.controller;

import com.povod9.adcampaign.dto.ProductRequest;
import com.povod9.adcampaign.dto.ProductResponse;
import com.povod9.adcampaign.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest productRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(productRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> productById(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.productById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> allProduct(){
        return ResponseEntity.ok(service.allProduct());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProductById(@PathVariable("id") Long id, @RequestBody @Valid ProductRequest productRequest){
        return ResponseEntity.ok(service.updateProductById(id, productRequest));
    }
}
