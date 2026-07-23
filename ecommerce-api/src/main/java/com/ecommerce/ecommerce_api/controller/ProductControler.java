package com.ecommerce.ecommerce_api.controller;

import com.ecommerce.ecommerce_api.dto.ProductRequestDto;
import com.ecommerce.ecommerce_api.dto.ProductResponseDto;
import com.ecommerce.ecommerce_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductControler {

    private final ProductService service;

    public ProductControler(ProductService service){
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto product){
        return ResponseEntity.ok(service.createProduct(product));
    }

    @GetMapping()
    public ResponseEntity<List<ProductResponseDto>> getAllProduct(){
        return ResponseEntity.ok(service.getAllProducts());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable("id") Long id,
                                                            @RequestBody ProductRequestDto dto){

        return ResponseEntity.ok(service.updateProduct(id, dto));
    }

}
