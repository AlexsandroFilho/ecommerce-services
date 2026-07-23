package com.inventory.inventory_api.mapper;

import com.inventory.inventory_api.dto.InventoryRequestDto;
import com.inventory.inventory_api.dto.InventoryResponseDto;
import com.inventory.inventory_api.model.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InventoryMapperTest {

    private InventoryRequestDto requestDto;
    private Inventory inventoryWithStock;
    private Inventory inventoryZeroStock;
    private Inventory inventoryNullQuantity;

    @BeforeEach
    void setUp() {
        requestDto = new InventoryRequestDto(1L, 10);

        inventoryWithStock = new Inventory(1L, 10, LocalDateTime.now());
        inventoryWithStock.setId(100L);

        inventoryZeroStock = new Inventory(2L, 0, LocalDateTime.now());
        inventoryZeroStock.setId(200L);

        inventoryNullQuantity = new Inventory(3L, null, LocalDateTime.now());
        inventoryNullQuantity.setId(300L);
    }

    @Test
    @DisplayName("Deve converter InventoryRequestDto para entidade Inventory")
    void shouldConvertDtoToEntity() {
        Inventory entity = InventoryMapper.toEntity(requestDto);

        assertNotNull(entity);
        assertEquals(requestDto.productId(), entity.getProductId());
        assertEquals(requestDto.quantity(), entity.getQuantity());
        assertNotNull(entity.getUpdateAt());
    }

    @Test
    @DisplayName("Deve converter Inventory para InventoryResponseDto com hasStock true")
    void shouldConvertEntityToResponseWithStockTrue() {
        InventoryResponseDto response = InventoryMapper.toResponse(inventoryWithStock);

        assertNotNull(response);
        assertEquals(100L, response.id());
        assertEquals(1L, response.productId());
        assertEquals(10, response.quantity());
        assertTrue(response.hasStock());
    }

    @Test
    @DisplayName("Deve converter Inventory para InventoryResponseDto com hasStock false quando quantidade for zero")
    void shouldConvertEntityToResponseWithStockFalseWhenZero() {
        InventoryResponseDto response = InventoryMapper.toResponse(inventoryZeroStock);

        assertNotNull(response);
        assertEquals(0, response.quantity());
        assertFalse(response.hasStock());
    }

    @Test
    @DisplayName("Deve converter Inventory para InventoryResponseDto com hasStock false quando quantidade for nula")
    void shouldConvertEntityToResponseWithStockFalseWhenNull() {
        InventoryResponseDto response = InventoryMapper.toResponse(inventoryNullQuantity);

        assertNotNull(response);
        assertNull(response.quantity());
        assertFalse(response.hasStock());
    }
}