package com.ecommerce.ecommerce_api.controller;

import tools.jackson.databind.ObjectMapper;
import com.ecommerce.ecommerce_api.dto.ProductRequestDto;
import com.ecommerce.ecommerce_api.dto.ProductResponseDto;
import com.ecommerce.ecommerce_api.exception.GlobalExceptionHandler;
import com.ecommerce.ecommerce_api.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce_api.factory.TestDataFactory;
import com.ecommerce.ecommerce_api.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductControler.class)
@Import(GlobalExceptionHandler.class)
class ProductControlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    private ProductRequestDto productRequest;
    private ProductResponseDto productResponse;
    private ProductRequestDto updateRequest;
    private ProductResponseDto updatedResponse;

    @BeforeEach
    void setUp() {
        productRequest = TestDataFactory.createProductRequestDto("Teclado", 150.0);
        productResponse = TestDataFactory.createProductResponseDto(1L, 150.0);
        updateRequest = TestDataFactory.createProductRequestDto("Teclado Novo", 200.0);
        updatedResponse = TestDataFactory.createProductResponseDto(1L, 200.0);
    }

    @Test
    @DisplayName("Deve criar produto com sucesso")
    void shouldCreateProductAndReturn200() throws Exception {
        when(productService.createProduct(any(ProductRequestDto.class))).thenReturn(productResponse);

        mockMvc.perform(post("/product/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.price").value(150.0));

        verify(productService, times(1)).createProduct(any(ProductRequestDto.class));
    }

    @Test
    @DisplayName("Deve retornar a lista de produtos")
    void shouldGetAllProductsAndReturn200() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(productResponse));

        mockMvc.perform(get("/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    @DisplayName("Deve atualizar o produto")
    void shouldUpdateProductAndReturn200() throws Exception {
        when(productService.updateProduct(eq(1L), any(ProductRequestDto.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/product/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(200.0));

        verify(productService, times(1)).updateProduct(eq(1L), any(ProductRequestDto.class));
    }

    @Test
    @DisplayName("Deve retornar 404 se produto nao existir")
    void shouldReturn404WhenUpdatingNonExistingProduct() throws Exception {
        when(productService.updateProduct(eq(99L), any(ProductRequestDto.class)))
                .thenThrow(new ResourceNotFoundException("Produto nao encontrado"));

        mockMvc.perform(put("/product/update/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Produto nao encontrado"));

        verify(productService, times(1)).updateProduct(eq(99L), any(ProductRequestDto.class));
    }
}