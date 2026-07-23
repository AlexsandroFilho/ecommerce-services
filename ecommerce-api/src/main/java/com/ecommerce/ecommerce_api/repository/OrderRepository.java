package com.ecommerce.ecommerce_api.repository;

import com.ecommerce.ecommerce_api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {}
