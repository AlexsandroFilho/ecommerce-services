package com.inventory.inventory_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "inventories")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long productId;

    private Integer quantity;
    private LocalDateTime updateAt;

    public Inventory(Long productId, Integer quantity, LocalDateTime updateAt) {
        this.productId = productId;
        this.quantity = quantity;
        this.updateAt = updateAt;
    }

    public void addQuantity(Integer amount){
        this.quantity += amount;
        this.updateAt = LocalDateTime.now();
    }

    public void subtractQuantity(Integer amount){
        this.quantity -= amount;
        this.updateAt = LocalDateTime.now();
    }

}
