package com.ecommerce.ecommerce_api.model;

import com.ecommerce.ecommerce_api.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String custumerName;
    private String custumerEmail;
    private Long productId;
    private Integer quantity;
    private Double totalValue;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime createAt;

    public Order(String custumerName, String custumerEmail, Long productId, Integer quantity, Double totalValue, OrderStatus orderStatus, LocalDateTime createAt) {
        this.custumerName = custumerName;
        this.custumerEmail = custumerEmail;
        this.productId = productId;
        this.quantity = quantity;
        this.totalValue = totalValue;
        this.orderStatus = orderStatus;
        this.createAt = createAt;
    }

    public void setCustomerName(String alex) {
    }

    public void setCustomerEmail(String mail) {
    }

    public void setCreatedAt(LocalDateTime now) {
    }
}
