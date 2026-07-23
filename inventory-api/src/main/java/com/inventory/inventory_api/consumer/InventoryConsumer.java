package com.inventory.inventory_api.consumer;

import com.inventory.inventory_api.dto.InventoryRequestDto;
import com.inventory.inventory_api.event.OrderCreatedEvent;
import com.inventory.inventory_api.event.ProductCreatedEvent;
import com.inventory.inventory_api.model.Inventory;
import com.inventory.inventory_api.service.InventoryService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InventoryConsumer {

    private final InventoryService service;

    public InventoryConsumer(InventoryService service){
        this.service = service;
    }

    @RabbitListener(queues = "inventory.product.create.queue")
    public void receiveProductCreated(ProductCreatedEvent event){
        System.out.println("Mensagem recebida! Inicializando estoque para o produto: " + event.productId());

        InventoryRequestDto dto = new InventoryRequestDto(event.productId(), 0);
        service.supplyStock(dto);
    }

    @RabbitListener(queues = "inventory.order.reserve.queue")
    public void receivedOrderCreated(OrderCreatedEvent event){
        System.out.println("Mensagem recebida! Reservando " + event.quantity() + " do produto " + event.productId());

        service.reserveStock(event.productId(), event.quantity());
    }
}
