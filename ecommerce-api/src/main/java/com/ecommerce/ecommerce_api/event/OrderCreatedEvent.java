package com.ecommerce.ecommerce_api.event;

public record OrderCreatedEvent(
        Long productId,
        Integer quantity
) {}
