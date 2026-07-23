package com.ecommerce.ecommerce_api.controller;

import tools.jackson.databind.ObjectMapper;
import com.ecommerce.ecommerce_api.dto.OrderRequestDto;
import com.ecommerce.ecommerce_api.dto.OrderResponseDto;
import com.ecommerce.ecommerce_api.enums.OrderStatus;
import com.ecommerce.ecommerce_api.exception.GlobalExceptionHandler;
import com.ecommerce.ecommerce_api.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce_api.factory.TestDataFactory;
import com.ecommerce.ecommerce_api.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@Import(GlobalExceptionHandler.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    private OrderRequestDto orderRequest;
    private OrderResponseDto orderResponse;
    private OrderRequestDto notFoundRequest;

    @BeforeEach
    void setUp() {
        orderRequest = TestDataFactory.createOrderRequestDto(1L, 2);
        notFoundRequest = TestDataFactory.createOrderRequestDto(99L, 1);
        orderResponse = new OrderResponseDto(
                UUID.randomUUID(),
                "Alex",
                "alex@email.com",
                1L,
                2,
                200.0,
                OrderStatus.PROCESSADO,
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Deve criar pedido e retornar HTTP 200 OK")
    void shouldCreateOrderAndReturn200() throws Exception {
        when(orderService.createOrder(any(OrderRequestDto.class))).thenReturn(orderResponse);

        mockMvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Alex"))
                .andExpect(jsonPath("$.totalValue").value(200.0))
                .andExpect(jsonPath("$.orderStatus").value("PROCESSADO"));

        verify(orderService, times(1)).createOrder(any(OrderRequestDto.class));
    }

    @Test
    @DisplayName("Deve listar todos os pedidos e retornar HTTP 200 OK")
    void shouldGetAllOrdersAndReturn200() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(orderResponse));

        mockMvc.perform(get("/order")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Alex"));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    @DisplayName("Deve retornar HTTP 404 via ExceptionHandler se o produto nao for encontrado")
    void shouldReturn404WhenProductNotFoundInService() throws Exception {
        when(orderService.createOrder(any(OrderRequestDto.class)))
                .thenThrow(new ResourceNotFoundException("Produto nao encontrado"));

        mockMvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notFoundRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Produto nao encontrado"));

        verify(orderService, times(1)).createOrder(any(OrderRequestDto.class));
    }
}