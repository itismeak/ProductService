package com.microservice.product_service.module.product.entity;

import com.microservice.product_service.common.entity.BaseEntity;
import com.microservice.product_service.common.enums.ProductCategory;
import com.microservice.product_service.common.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String name;
    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Integer quantity;

    @Column(precision = 10, scale = 2,nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status; // optional enum

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;
}