package com.microservice.product_service.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderSaveEvent {
    private Long orderId;
    private Long productId;
    private int quantity;
}