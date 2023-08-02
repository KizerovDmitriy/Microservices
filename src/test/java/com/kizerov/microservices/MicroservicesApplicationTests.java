package com.kizerov.microservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kizerov.microservices.dto.ProductRequest;
import com.kizerov.microservices.model.Product;
import com.kizerov.microservices.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class MicroservicesApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Container
    private static final MySQLContainer CONTAINER = new MySQLContainer("mysql:latest");

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", CONTAINER::getUsername);
        registry.add("spring.datasource.password", CONTAINER::getPassword);
    }
    @BeforeEach
    void setUpProducts() {

        productRepository.save(new Product(1L,"iphone","iphone 10",BigDecimal.valueOf(800)));
        productRepository.save(new Product(2L,"iphone","iphone 11",BigDecimal.valueOf(1000)));

    }

    @Test
    void shouldCreateProduct() throws Exception {

        String productRequest = objectMapper.writeValueAsString(getProductRequest());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequest))
                .andExpect(status().isCreated());

        Assertions.assertEquals(3,productRepository.findAll().size());

    }

    @Test
    void shouldGetAllProduct() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Assertions.assertEquals(2,productRepository.findAll().size());
    }

    private ProductRequest getProductRequest() {

        return ProductRequest.builder()
                .name("iPhone")
                .description("iPhone 13")
                .price(BigDecimal.valueOf(1100))
                .build();
    }

}
