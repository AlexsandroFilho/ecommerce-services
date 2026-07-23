package com.ecommerce.ecommerce_api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import static org.junit.jupiter.api.Assertions.*;

class OrderMessagingConfigTest {

    private OrderMessagingConfig config;

    @BeforeEach
    void setUp() {
        config = new OrderMessagingConfig();
    }

    @Test
    @DisplayName("Deve criar o DirectExchange do pedido com sucesso")
    void shouldCreateOrderExchange() {
        DirectExchange exchange = config.orderExchange();
        assertEquals("order.exchange", exchange.getName());
    }

    @Test
    @DisplayName("Deve criar o DirectExchange da DLX do pedido com sucesso")
    void shouldCreateOrderDlx() {
        DirectExchange exchange = config.orderDlx();
        assertEquals("order.dlx", exchange.getName());
    }

    @Test
    @DisplayName("Deve criar a fila de reserva de pedido com configuracoes de DLX")
    void shouldCreateOrderReserveQueue() {
        Queue queue = config.orderReserveQueue();
        assertEquals("inventory.order.reserve.queue", queue.getName());
        assertTrue(queue.isDurable());
        assertEquals("order.dlx", queue.getArguments().get("x-dead-letter-exchange"));
        assertEquals("order.created.dlq", queue.getArguments().get("x-dead-letter-routing-key"));
    }

    @Test
    @DisplayName("Deve criar a fila de DLQ de reserva de pedido com sucesso")
    void shouldCreateOrderReserveDlq() {
        Queue queue = config.orderReserveDlq();
        assertEquals("inventory.order.reserve.dlq", queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    @DisplayName("Deve criar o binding da fila de criacao de pedido com sucesso")
    void shouldCreateBindingOrderCreate() {
        Binding binding = config.bindingOrderCreate();
        assertEquals("inventory.order.reserve.queue", binding.getDestination());
        assertEquals("order.exchange", binding.getExchange());
        assertEquals("order.created", binding.getRoutingKey());
    }

    @Test
    @DisplayName("Deve criar o binding da DLQ de pedido com sucesso")
    void shouldCreateBindingOrderReserveDlq() {
        Binding binding = config.bindingOrderReserveDlq();
        assertEquals("inventory.order.reserve.dlq", binding.getDestination());
        assertEquals("order.dlx", binding.getExchange());
        assertEquals("order.created.dlq", binding.getRoutingKey());
    }
}