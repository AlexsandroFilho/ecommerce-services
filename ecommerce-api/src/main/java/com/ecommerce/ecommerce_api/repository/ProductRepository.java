package com.ecommerce.ecommerce_api.repository;

import com.ecommerce.ecommerce_api.model.Product;
import org.springframework.data.jpa.mapping.JpaPersistentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> { }
