package com.ecommerce.ecommerce_api.controller;

import com.ecommerce.ecommerce_api.dto.OrderRequestDto;
import com.ecommerce.ecommerce_api.dto.OrderResponseDto;
import com.ecommerce.ecommerce_api.service.OrderService;
import com.ecommerce.ecommerce_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto dto){
        return ResponseEntity.ok(service.createOrder(dto));
    }

    @GetMapping()
    public ResponseEntity<List<OrderResponseDto>> getAllOrder(){
        return ResponseEntity.ok(service.getAllOrders());
    }

}
