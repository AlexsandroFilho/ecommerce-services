package com.inventory.inventory_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record InventoryRequestDto(
        @NotNull(message = "Id do produto é obrigatório")
        Long productId,

        @NotNull(message = "Quantidade é obrigatória")
        @Positive
        Integer quantity

) {}
