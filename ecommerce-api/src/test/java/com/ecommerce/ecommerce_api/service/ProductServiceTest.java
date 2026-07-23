package com.ecommerce.ecommerce_api.service;

import com.ecommerce.ecommerce_api.dto.ProductRequestDto;
import com.ecommerce.ecommerce_api.dto.ProductResponseDto;
import com.ecommerce.ecommerce_api.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce_api.factory.TestDataFactory;
import com.ecommerce.ecommerce_api.model.Product;
import com.ecommerce.ecommerce_api.producer.NotificationProducer;
import com.ecommerce.ecommerce_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private NotificationProducer notificationProducer;

    @InjectMocks
    private ProductService productService;

    private Product productKeyboard;
    private Product productMouse;
    private Product savedMonitor;
    private ProductRequestDto createMonitorRequest;
    private ProductRequestDto updateKeyboardRequest;
    private ProductRequestDto updateNotFoundRequest;

    @BeforeEach
    void setUp() {
        productKeyboard = TestDataFactory.createProduct(1L, "Teclado", 150.0);
        productMouse = TestDataFactory.createProduct(2L, "Mouse", 80.0);
        savedMonitor = TestDataFactory.createProduct(10L, "Monitor", 1200.0);

        createMonitorRequest = TestDataFactory.createProductRequestDto("Monitor", 1200.0);
        updateKeyboardRequest = TestDataFactory.createProductRequestDto("Teclado Mecanico Novo", 250.0);
        updateNotFoundRequest = TestDataFactory.createProductRequestDto("Inexistente", 100.0);
    }

    @Test
    @DisplayName("Deve retornar todos os produtos")
    void shouldReturnAllProducts() {
        when(repository.findAll()).thenReturn(List.of(productKeyboard, productMouse));

        List<ProductResponseDto> result = productService.getAllProducts();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar um produto por ID existente")
    void shouldReturnProductWhenIdExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(productKeyboard));

        ProductResponseDto response = productService.getProduct(1L);

        assertNotNull(response);
        assertEquals("Teclado", response.name());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lancar excecao quando o produto por ID nao existir")
    void shouldThrowExceptionWhenProductNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProduct(99L));
        verify(repository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve criar produto com sucesso e disparar notificacao")
    void shouldCreateProductSuccessfully() {
        when(repository.save(any(Product.class))).thenReturn(savedMonitor);

        ProductResponseDto response = productService.createProduct(createMonitorRequest);

        assertNotNull(response);
        assertEquals(10L, response.id());
        verify(repository, times(1)).save(any(Product.class));
        verify(notificationProducer, times(1)).sendCreateProduct(10L);
    }

    @Test
    @DisplayName("Deve atualizar um produto existente")
    void shouldUpdateProductSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(productKeyboard));
        when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponseDto response = productService.updateProduct(1L, updateKeyboardRequest);

        assertEquals("Teclado Mecanico Novo", response.name());
        assertEquals(250.0, response.price());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(productKeyboard);
    }

    @Test
    @DisplayName("Nao deve atualizar produto inexistente")
    void shouldThrowExceptionWhenUpdatingNonExistingProduct() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(99L, updateNotFoundRequest));

        verify(repository, times(1)).findById(99L);
        verify(repository, never()).save(any());
    }
}