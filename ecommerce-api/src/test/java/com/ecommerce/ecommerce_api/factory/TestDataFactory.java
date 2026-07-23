package com.ecommerce.ecommerce_api.factory;

import com.ecommerce.ecommerce_api.dto.OrderRequestDto;
import com.ecommerce.ecommerce_api.dto.ProductRequestDto;
import com.ecommerce.ecommerce_api.dto.ProductResponseDto;
import com.ecommerce.ecommerce_api.enums.Category;
import com.ecommerce.ecommerce_api.enums.OrderStatus;
import com.ecommerce.ecommerce_api.model.Order;
import com.ecommerce.ecommerce_api.model.Product;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestDataFactory {

    public static Product createProduct(Long id, String name, double price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription("Descrição do produto " + name);
        product.setPrice(price);
        product.setCategory(Category.ELETRONICOS);
        product.setCreatedAt(LocalDateTime.now());
        return product;
    }

    public static ProductRequestDto createProductRequestDto(String name, double price) {
        return new ProductRequestDto(name, "Descrição do produto", price, Category.ELETRONICOS);
    }

    public static ProductResponseDto createProductResponseDto(Long id, double price) {
        return new ProductResponseDto(id, "Produto " + id, "Descrição", price, Category.ELETRONICOS, LocalDateTime.now());
    }

    public static OrderRequestDto createOrderRequestDto(Long productId, int quantity) {
        return new OrderRequestDto("Alex", "alex@email.com", productId, quantity);
    }

    public static Order createOrder(Long productId, int quantity, double totalValue, OrderStatus status) {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setCustomerName("Alex");
        order.setCustomerEmail("alex@email.com");
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotalValue(totalValue);
        order.setOrderStatus(status);
        order.setCreatedAt(LocalDateTime.now());
        return order;
    }

}
