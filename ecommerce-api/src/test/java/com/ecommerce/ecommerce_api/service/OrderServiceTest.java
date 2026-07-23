package com.ecommerce.ecommerce_api.service;

import com.ecommerce.ecommerce_api.dto.OrderRequestDto;
import com.ecommerce.ecommerce_api.dto.OrderResponseDto;
import com.ecommerce.ecommerce_api.dto.ProductResponseDto;
import com.ecommerce.ecommerce_api.enums.OrderStatus;
import com.ecommerce.ecommerce_api.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce_api.factory.TestDataFactory;
import com.ecommerce.ecommerce_api.model.Order;
import com.ecommerce.ecommerce_api.producer.NotificationProducer;
import com.ecommerce.ecommerce_api.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private NotificationProducer notificationProducer;

    @InjectMocks
    private OrderService orderService;

    private Order orderProcessed;
    private Order orderInAnalysis;
    private OrderRequestDto standardOrderRequest;
    private OrderRequestDto singleItemOrderRequest;
    private OrderRequestDto notFoundOrderRequest;
    private ProductResponseDto cheapProduct;
    private ProductResponseDto midPriceProduct;
    private ProductResponseDto expensiveProduct;

    @BeforeEach
    void setUp() {
        orderProcessed = TestDataFactory.createOrder(1L, 2, 200.0, OrderStatus.PROCESSADO);
        orderInAnalysis = TestDataFactory.createOrder(2L, 1, 3000.0, OrderStatus.EM_ANALISE);

        standardOrderRequest = TestDataFactory.createOrderRequestDto(1L, 2);
        singleItemOrderRequest = TestDataFactory.createOrderRequestDto(1L, 1);
        notFoundOrderRequest = TestDataFactory.createOrderRequestDto(99L, 1);

        cheapProduct = TestDataFactory.createProductResponseDto(1L, 100.0);
        midPriceProduct = TestDataFactory.createProductResponseDto(1L, 1000.0);
        expensiveProduct = TestDataFactory.createProductResponseDto(1L, 3000.0);
    }

    @Test
    @DisplayName("Deve buscar e listar todos os pedidos")
    void shouldReturnAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(orderProcessed, orderInAnalysis));

        List<OrderResponseDto> result = orderService.getAllOrders();

        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Regra: Valor <= 500 -> Sem desconto e status PROCESSADO")
    void shouldCreateOrderWithoutDiscountAndStatusProcessado() {
        when(productService.getProduct(1L)).thenReturn(cheapProduct);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> {
            Order o = i.getArgument(0);
            o.setId(UUID.randomUUID());
            return o;
        });

        OrderResponseDto response = orderService.createOrder(standardOrderRequest);

        assertEquals(200.0, response.totalValue());
        assertEquals(OrderStatus.PROCESSADO, response.orderStatus());

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(notificationProducer, times(1)).sendCreateOrder(eq(1L), eq(2));
    }

    @Test
    @DisplayName("Regra: Valor > 500 e <= 2000 -> Com 5% desconto e status PROCESSADO")
    void shouldCreateOrderWithDiscountAndStatusProcessado() {
        when(productService.getProduct(1L)).thenReturn(midPriceProduct);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> {
            Order o = i.getArgument(0);
            o.setId(UUID.randomUUID());
            return o;
        });

        OrderResponseDto response = orderService.createOrder(singleItemOrderRequest);

        assertEquals(950.0, response.totalValue());
        assertEquals(OrderStatus.PROCESSADO, response.orderStatus());

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(notificationProducer, times(1)).sendCreateOrder(eq(1L), eq(1));
    }

    @Test
    @DisplayName("Regra: Valor > 2000 -> Com 5% desconto e status EM_ANALISE")
    void shouldCreateOrderWithDiscountAndStatusEmAnalise() {
        when(productService.getProduct(1L)).thenReturn(expensiveProduct);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> {
            Order o = i.getArgument(0);
            o.setId(UUID.randomUUID());
            return o;
        });

        OrderResponseDto response = orderService.createOrder(singleItemOrderRequest);

        assertEquals(2850.0, response.totalValue());
        assertEquals(OrderStatus.EM_ANALISE, response.orderStatus());

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(notificationProducer, times(1)).sendCreateOrder(eq(1L), eq(1));
    }

    @Test
    @DisplayName("Falha: Nao deve salvar nem enviar mensagem se o produto nao for encontrado")
    void shouldNotCreateOrderWhenProductNotFound() {
        when(productService.getProduct(99L)).thenThrow(new ResourceNotFoundException("Produto nao encontrado"));

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(notFoundOrderRequest));

        verify(orderRepository, never()).save(any());
        verify(notificationProducer, never()).sendCreateOrder(any(), anyInt());
    }
}