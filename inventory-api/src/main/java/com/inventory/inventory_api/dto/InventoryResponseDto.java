package com.inventory.inventory_api.dto;

import java.time.LocalDateTime;

public record InventoryResponseDto(
        Long id,
        Long productId,
        Integer quantity,
        Boolean hasStock,
        LocalDateTime updateAt
) {}
