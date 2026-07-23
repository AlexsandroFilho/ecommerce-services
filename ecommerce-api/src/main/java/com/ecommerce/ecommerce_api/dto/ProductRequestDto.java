package com.ecommerce.ecommerce_api.dto;

import com.ecommerce.ecommerce_api.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequestDto(
    @NotBlank(message = "O nome do produto é obrigatório")
    String name,
    String description,

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser maior que 0")
    Double price,

    @NotNull(message = "Categoria é obrigatória")
    Category category
) {}
