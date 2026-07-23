package com.ecommerce.ecommerce_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequestDto(
        @NotBlank(message = "O nome do cliente é obrigatório")
        String customerName,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Formato de email inválido")
        String customerEmail,

        @NotNull(message = "O ID do produto é obrigatório")
        Long productId,

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantity
){}
