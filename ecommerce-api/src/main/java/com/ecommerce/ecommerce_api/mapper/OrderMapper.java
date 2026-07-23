package com.ecommerce.ecommerce_api.mapper;

import com.ecommerce.ecommerce_api.dto.OrderRequestDto;
import com.ecommerce.ecommerce_api.dto.OrderResponseDto;
import com.ecommerce.ecommerce_api.enums.OrderStatus;
import com.ecommerce.ecommerce_api.model.Order;

import java.time.LocalDateTime;

public class OrderMapper {

    public static Order toEntity(OrderRequestDto dto, Double totalValue, OrderStatus status){
        return new Order(
                dto.customerName(),
                dto.customerEmail(),
                dto.productId(),
                dto.quantity(),
                totalValue,
                status,
                LocalDateTime.now()
        );
    }

    public static OrderResponseDto toResponse(Order order){
        return new OrderResponseDto(
                order.getId(),
                order.getCustumerName(),
                order.getCustumerEmail(),
                order.getProductId(),
                order.getQuantity(),
                order.getTotalValue(),
                order.getOrderStatus(),
                order.getCreateAt()
        );
    }
}
