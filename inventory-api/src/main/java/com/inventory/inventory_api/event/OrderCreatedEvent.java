package com.inventory.inventory_api.event;

public record OrderCreatedEvent(
        Long productId,
        Integer quantity
) {}
