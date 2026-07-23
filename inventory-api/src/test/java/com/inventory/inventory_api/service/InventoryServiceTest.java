package com.inventory.inventory_api.service;

import com.inventory.inventory_api.dto.InventoryRequestDto;
import com.inventory.inventory_api.dto.InventoryResponseDto;
import com.inventory.inventory_api.exception.BusinessRuleException;
import com.inventory.inventory_api.exception.ResourceNotFoundException;
import com.inventory.inventory_api.model.Inventory;
import com.inventory.inventory_api.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository repository;

    @InjectMocks
    private InventoryService service;

    private Inventory existingInventory;
    private Inventory savedInventory;
    private InventoryRequestDto supplyDto;
    private Long existingProductId;
    private Long nonExistingProductId;

    @BeforeEach
    void setUp() {
        existingProductId = 1L;
        nonExistingProductId = 99L;
        existingInventory = new Inventory(existingProductId, 10, LocalDateTime.now());
        existingInventory.setId(100L);

        savedInventory = new Inventory(existingProductId, 20, LocalDateTime.now());
        savedInventory.setId(100L);

        supplyDto = new InventoryRequestDto(existingProductId, 10);
    }

    @Test
    @DisplayName("Deve buscar estoque existente por ID do produto")
    void shouldGetStockWhenProductExists() {
        when(repository.findByProductId(existingProductId)).thenReturn(Optional.of(existingInventory));

        InventoryResponseDto response = service.getStock(existingProductId);

        assertNotNull(response);
        assertEquals(existingProductId, response.productId());
        assertEquals(10, response.quantity());
        assertTrue(response.hasStock());
        verify(repository, times(1)).findByProductId(existingProductId);
    }

    @Test
    @DisplayName("Deve criar novo registro com estoque zero quando produto nao existir no getStock")
    void shouldCreateNewInventoryWhenProductDoesNotExistOnGetStock() {
        when(repository.findByProductId(nonExistingProductId)).thenReturn(Optional.empty());

        InventoryResponseDto response = service.getStock(nonExistingProductId);

        assertNotNull(response);
        assertEquals(nonExistingProductId, response.productId());
        assertEquals(0, response.quantity());
        assertFalse(response.hasStock());
        verify(repository, times(1)).findByProductId(nonExistingProductId);
    }

    @Test
    @DisplayName("Deve abastecer estoque com sucesso")
    void shouldSupplyStockSuccessfully() {
        when(repository.findByProductId(existingProductId)).thenReturn(Optional.of(existingInventory));
        when(repository.save(any(Inventory.class))).thenReturn(savedInventory);

        InventoryResponseDto response = service.supplyStock(supplyDto);

        assertNotNull(response);
        assertEquals(20, response.quantity());
        verify(repository, times(1)).findByProductId(existingProductId);
        verify(repository, times(1)).save(existingInventory);
    }

    @Test
    @DisplayName("Deve lancar ResourceNotFoundException ao tentar abastecer produto inexistente")
    void shouldThrowExceptionWhenSupplyingNonExistingProduct() {
        InventoryRequestDto invalidDto = new InventoryRequestDto(nonExistingProductId, 5);
        when(repository.findByProductId(nonExistingProductId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.supplyStock(invalidDto));

        verify(repository, times(1)).findByProductId(nonExistingProductId);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve reservar estoque com sucesso")
    void shouldReserveStockSuccessfully() {
        when(repository.findByProductId(existingProductId)).thenReturn(Optional.of(existingInventory));
        when(repository.save(any(Inventory.class))).thenAnswer(i -> i.getArgument(0));

        InventoryResponseDto response = service.reserveStock(existingProductId, 5);

        assertNotNull(response);
        assertEquals(5, response.quantity());
        verify(repository, times(1)).findByProductId(existingProductId);
        verify(repository, times(1)).save(existingInventory);
    }

    @Test
    @DisplayName("Deve lancar BusinessRuleException ao tentar reservar quantidade maior do que o estoque disponivel")
    void shouldThrowBusinessRuleExceptionWhenStockIsInsufficient() {
        when(repository.findByProductId(existingProductId)).thenReturn(Optional.of(existingInventory));

        assertThrows(BusinessRuleException.class, () -> service.reserveStock(existingProductId, 50));

        verify(repository, times(1)).findByProductId(existingProductId);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lancar ResourceNotFoundException ao tentar reservar produto inexistente")
    void shouldThrowExceptionWhenReservingNonExistingProduct() {
        when(repository.findByProductId(nonExistingProductId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.reserveStock(nonExistingProductId, 5));

        verify(repository, times(1)).findByProductId(nonExistingProductId);
        verify(repository, never()).save(any());
    }
}