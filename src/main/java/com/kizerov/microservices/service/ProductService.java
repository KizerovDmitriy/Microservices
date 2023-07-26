package com.kizerov.microservices.service;

import com.kizerov.microservices.dto.ProductRequest;
import com.kizerov.microservices.dto.ProductResponse;
import com.kizerov.microservices.model.Product;
import com.kizerov.microservices.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createNewProduct(ProductRequest productRequest) {

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);

        log.info("Product {} is saved",product.getId());
    }

    public List<ProductResponse> getAllProducts() {

        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(this::productToResponse)
                .toList();
    }

    private ProductResponse productToResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
