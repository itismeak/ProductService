package com.microservice.product_service.common.dto;

import com.microservice.product_service.common.enums.ProductCategory;
import com.microservice.product_service.common.enums.ProductStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDto {

    @NotNull
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    @Positive
    private Integer quantity;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotNull
    private ProductStatus status;

    @NotNull
    private ProductCategory category;
}