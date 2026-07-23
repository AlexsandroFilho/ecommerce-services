package com.inventory.inventory_api.consumer;

import com.inventory.inventory_api.dto.InventoryRequestDto;
import com.inventory.inventory_api.event.OrderCreatedEvent;
import com.inventory.inventory_api.event.ProductCreatedEvent;
import com.inventory.inventory_api.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InventoryConsumerTest {

    @Mock
    private InventoryService service;

    @InjectMocks
    private InventoryConsumer consumer;

    private ProductCreatedEvent productEvent;
    private OrderCreatedEvent orderEvent;
    private InventoryRequestDto expectedSupplyDto;

    @BeforeEach
    void setUp() {
        productEvent = new ProductCreatedEvent(10L);
        orderEvent = new OrderCreatedEvent(10L, 5);
        expectedSupplyDto = new InventoryRequestDto(10L, 0);
    }

    @Test
    @DisplayName("Deve processar evento de produto criado e abastecer estoque inicial")
    void shouldReceiveProductCreatedAndSupplyInitialStock() {
        consumer.receiveProductCreated(productEvent);

        verify(service).supplyStock(eq(expectedSupplyDto));
    }

    @Test
    @DisplayName("Deve processar evento de pedido criado e reservar estoque")
    void shouldReceiveOrderCreatedAndReserveStock() {
        consumer.receivedOrderCreated(orderEvent);

        verify(service).reserveStock(eq(10L), eq(5));
    }
}