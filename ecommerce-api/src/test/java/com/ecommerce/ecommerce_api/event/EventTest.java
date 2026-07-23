package com.ecommerce.ecommerce_api.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EventTest {

    private final Long productId = 1L;
    private final Integer quantity = 2;

    @Test
    @DisplayName("Deve validar a criacao e acessores do ProductCreatedEvent")
    void shouldValidateProductCreatedEvent() {
        ProductCreatedEvent event = new ProductCreatedEvent(productId);

        assertEquals(productId, event.productId());
        assertNotNull(event.toString());
    }

    @Test
    @DisplayName("Deve validar a criacao e acessores do OrderCreatedEvent")
    void shouldValidateOrderCreatedEvent() {
        OrderCreatedEvent event = new OrderCreatedEvent(productId, quantity);

        assertEquals(productId, event.productId());
        assertEquals(quantity, event.quantity());
        assertNotNull(event.toString());
    }
}