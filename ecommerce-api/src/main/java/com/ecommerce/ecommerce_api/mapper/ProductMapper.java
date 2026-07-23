package com.ecommerce.ecommerce_api.mapper;

import com.ecommerce.ecommerce_api.dto.ProductRequestDto;
import com.ecommerce.ecommerce_api.dto.ProductResponseDto;
import com.ecommerce.ecommerce_api.model.Product;

import java.time.LocalDateTime;

public class ProductMapper {

    public static Product toEntity(ProductRequestDto dto){
        return new Product(
                dto.name(),
                dto.description(),
                dto.price(),
                dto.category(),
                LocalDateTime.now()
        );
    }

    public static ProductResponseDto toResponse(Product product){
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getCreateAt()
        );
    }

}
