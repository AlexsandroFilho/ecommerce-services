package com.ecommerce.ecommerce_api.service;

import com.ecommerce.ecommerce_api.dto.OrderRequestDto;
import com.ecommerce.ecommerce_api.dto.OrderResponseDto;
import com.ecommerce.ecommerce_api.dto.ProductResponseDto;
import com.ecommerce.ecommerce_api.enums.OrderStatus;
import com.ecommerce.ecommerce_api.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce_api.mapper.OrderMapper;
import com.ecommerce.ecommerce_api.model.Order;
import com.ecommerce.ecommerce_api.model.Product;
import com.ecommerce.ecommerce_api.producer.NotificationProducer;
import com.ecommerce.ecommerce_api.repository.OrderRepository;
import com.ecommerce.ecommerce_api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final NotificationProducer notificationProducer;

    public OrderService(OrderRepository orderRepository, ProductService productService, NotificationProducer notificationProducer) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.notificationProducer = notificationProducer;
    }

    public List<OrderResponseDto> getAllOrders(){
        return orderRepository.findAll().stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    public OrderResponseDto createOrder(OrderRequestDto dto){
        ProductResponseDto product = productService.getProduct(dto.productId());

        double finalTotalValue = calculeTotalValue(product.price(), dto.quantity());

        OrderStatus status = defineOrderStatus(finalTotalValue);

        Order order = OrderMapper.toEntity(dto, finalTotalValue, status);
        Order savedOrder = orderRepository.save(order);

        notificationProducer.sendCreateOrder(savedOrder.getProductId(), savedOrder.getQuantity());

        return OrderMapper.toResponse(savedOrder);
    }

    private double calculeTotalValue(double price, int quantity){
        double rawTotal = price * quantity;
        return (rawTotal > 500.00) ? rawTotal * 0.95 : rawTotal;
    }

    private OrderStatus defineOrderStatus(double totalValue){
        return (totalValue > 2000.00) ? OrderStatus.EM_ANALISE : OrderStatus.PROCESSADO;
    }

}
