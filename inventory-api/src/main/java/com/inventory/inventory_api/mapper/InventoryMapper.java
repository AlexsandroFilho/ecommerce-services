package com.inventory.inventory_api.mapper;

import com.inventory.inventory_api.dto.InventoryRequestDto;
import com.inventory.inventory_api.dto.InventoryResponseDto;
import com.inventory.inventory_api.model.Inventory;

import java.time.LocalDateTime;

public class InventoryMapper {

    public static Inventory toEntity(InventoryRequestDto dto){
        return new Inventory(
                dto.productId(),
                dto.quantity(),
                LocalDateTime.now()
        );
    }

    public static InventoryResponseDto toResponse(Inventory inventory){
        boolean hasStock = inventory.getQuantity() != null && inventory.getQuantity() > 0;

        return new InventoryResponseDto(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getQuantity(),
                hasStock,
                inventory.getUpdateAt()
        );
    }

}
