package com.ecommerce.ecommerce_api.dto;

import com.ecommerce.ecommerce_api.enums.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDateTime;

public record ProductResponseDto(
        Long id,
        String name,
        String description,
        Double price,
        Category category,
        LocalDateTime creatAt
) {}
