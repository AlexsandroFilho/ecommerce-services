package com.ecommerce.ecommerce_api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import static org.junit.jupiter.api.Assertions.*;

class ProductMessagingConfigTest {

    private ProductMessagingConfig config;

    @BeforeEach
    void setUp() {
        config = new ProductMessagingConfig();
    }

    @Test
    @DisplayName("Deve criar o DirectExchange do produto com sucesso")
    void shouldCreateProductExchange() {
        DirectExchange exchange = config.productExchange();
        assertEquals("product.exchange", exchange.getName());
    }

    @Test
    @DisplayName("Deve criar o DirectExchange da DLX do produto com sucesso")
    void shouldCreateProductDlx() {
        DirectExchange exchange = config.productDlx();
        assertEquals("product.dlx", exchange.getName());
    }

    @Test
    @DisplayName("Deve criar a fila de criacao de produto com configuracoes de DLX")
    void shouldCreateProductCreateQueue() {
        Queue queue = config.productCreateQueue();
        assertEquals("inventory.product.create.queue", queue.getName());
        assertTrue(queue.isDurable());
        assertEquals("product.dlx", queue.getArguments().get("x-dead-letter-exchange"));
        assertEquals("product.created.dlq", queue.getArguments().get("x-dead-letter-routing-key"));
    }

    @Test
    @DisplayName("Deve criar a fila de DLQ de criacao de produto com sucesso")
    void shouldCreateProductCreateDlq() {
        Queue queue = config.productCreateDlq();
        assertEquals("inventory.product.create.dlq", queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    @DisplayName("Deve criar o binding da fila de criacao de produto com sucesso")
    void shouldCreateBindingProductCreate() {
        Binding binding = config.bindingProductCreate();
        assertEquals("inventory.product.create.queue", binding.getDestination());
        assertEquals("product.exchange", binding.getExchange());
        assertEquals("product.created", binding.getRoutingKey());
    }

    @Test
    @DisplayName("Deve criar o binding da DLQ de produto com sucesso")
    void shouldCreateBindingProductCreateDlq() {
        Binding binding = config.bindingProductCreateDlq();
        assertEquals("inventory.product.create.dlq", binding.getDestination());
        assertEquals("product.dlx", binding.getExchange());
        assertEquals("product.created.dlq", binding.getRoutingKey());
    }
}