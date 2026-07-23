package com.ecommerce.ecommerce_api.model;

import com.ecommerce.ecommerce_api.dto.ProductRequestDto;
import com.ecommerce.ecommerce_api.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;

    @Enumerated(EnumType.STRING)
    private Category category;

    private LocalDateTime createAt;

    public Product(String name, String description, Double price, Category category, LocalDateTime createAt) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.createAt = createAt;
    }

    public void updateFromDto(ProductRequestDto dto){
        this.name = dto.name();
        this.description = dto.description();
        this.price = dto.price();
        this.category = dto.category();
    }

    public void setCreatedAt(LocalDateTime now) {

    }
}
