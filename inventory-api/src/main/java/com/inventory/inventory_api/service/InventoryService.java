package com.inventory.inventory_api.service;

import com.inventory.inventory_api.annotation.AuditLog;
import com.inventory.inventory_api.dto.InventoryRequestDto;
import com.inventory.inventory_api.dto.InventoryResponseDto;
import com.inventory.inventory_api.exception.BusinessRuleException;
import com.inventory.inventory_api.exception.ResourceNotFoundException;
import com.inventory.inventory_api.mapper.InventoryMapper;
import com.inventory.inventory_api.model.Inventory;
import com.inventory.inventory_api.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository){
        this.repository = repository;
    }

    public InventoryResponseDto getStock(Long productId){
        Inventory inventory = findOrCreateInventory(productId);

        return InventoryMapper.toResponse(inventory);
    }

    @AuditLog(action = "ABASTECIMENTO_ESTOQUE")
    public InventoryResponseDto supplyStock(InventoryRequestDto dto){
        Inventory inventory = findInventoryOrThrow(dto.productId());

        inventory.addQuantity(dto.quantity());

        Inventory savedInventory = repository.save(inventory);
        return InventoryMapper.toResponse(savedInventory);
    }

    @AuditLog(action = "RESERVA_ESTOQUE")
    public InventoryResponseDto reserveStock(Long productId, Integer quantityToReserve){
        Inventory inventory = findInventoryOrThrow(productId);

        if(inventory.getQuantity() < quantityToReserve)
            throw new BusinessRuleException("Estoque insuficiente. Disponivel " + inventory.getQuantity());

        inventory.subtractQuantity(quantityToReserve);

        Inventory savedInventory = repository.save(inventory);
        return InventoryMapper.toResponse(savedInventory);
    }

    private Inventory findOrCreateInventory(Long productId){
        return repository.findByProductId(productId)
                .orElseGet(()-> new Inventory(productId, 0, LocalDateTime.now()));
    }

    private Inventory findInventoryOrThrow(Long productId){
        return repository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
    }
}