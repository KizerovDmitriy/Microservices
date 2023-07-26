package com.kizerov.microservices.controller;

import com.kizerov.microservices.dto.ProductRequest;
import com.kizerov.microservices.dto.ProductResponse;
import com.kizerov.microservices.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewProduct(@RequestBody ProductRequest productRequest) {

        productService.createNewProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {

        return productService.getAllProducts();
    }
}
