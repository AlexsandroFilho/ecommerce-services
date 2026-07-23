package com.ecommerce.ecommerce_api.dto;

import com.ecommerce.ecommerce_api.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponseDto(
        UUID id,
        String customerName,
        String customerEmail,
        Long productId,
        Integer quantity,
        Double totalValue,
        OrderStatus orderStatus,
        LocalDateTime createAt
) {}
