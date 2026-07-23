package com.ecommerce.ecommerce_api.service;

import com.ecommerce.ecommerce_api.dto.ProductRequestDto;
import com.ecommerce.ecommerce_api.dto.ProductResponseDto;
import com.ecommerce.ecommerce_api.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce_api.mapper.ProductMapper;
import com.ecommerce.ecommerce_api.model.Product;
import com.ecommerce.ecommerce_api.producer.NotificationProducer;
import com.ecommerce.ecommerce_api.repository.ProductRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final NotificationProducer notificationProducer;

    public ProductService(ProductRepository repository, NotificationProducer notificationProducer){
        this.repository = repository;
        this.notificationProducer = notificationProducer;
    }

    public List<ProductResponseDto> getAllProducts() {
        return repository.findAll().stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    public ProductResponseDto getProduct(Long id){
         return ProductMapper.toResponse(productFindById(id));
    }

    public ProductResponseDto createProduct(ProductRequestDto dto){
        Product product = ProductMapper.toEntity(dto);
        Product savedProduct = repository.save(product);

        notificationProducer.sendCreateProduct(savedProduct.getId());

        return ProductMapper.toResponse(savedProduct);
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto dto){
        Product existProduct = productFindById(id);

        existProduct.updateFromDto(dto);

        return ProductMapper.toResponse(repository.save(existProduct));
    }

    private Product productFindById(Long id) {
        return repository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Produto não encontrado"));
    }

}
