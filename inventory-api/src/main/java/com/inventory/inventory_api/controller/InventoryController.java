package com.inventory.inventory_api.controller;

import com.inventory.inventory_api.dto.InventoryRequestDto;
import com.inventory.inventory_api.dto.InventoryResponseDto;
import com.inventory.inventory_api.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @PostMapping("/supply")
    public ResponseEntity<InventoryResponseDto> supplyStock(@Valid @RequestBody InventoryRequestDto dto){
        return ResponseEntity.ok(service.supplyStock(dto));
    }

    @PostMapping("/reserve")
    public ResponseEntity<InventoryResponseDto> reserveStock(@RequestParam("productId") Long productId, @RequestParam("quantityToReserve") Integer quantityToReserve){
        return ResponseEntity.ok(service.reserveStock(productId, quantityToReserve));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponseDto> getStock(@PathVariable("productId") Long productId){
        return ResponseEntity.ok(service.getStock(productId));
    }

}
