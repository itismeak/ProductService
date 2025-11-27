package com.microservice.product_service.common.client.consumer;

import com.microservice.product_service.common.exceptions.ResourceNotFoundException;
import com.microservice.product_service.module.product.entity.Product;
import com.microservice.product_service.module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.events.OrderSaveEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventConsumer {
    private final ProductRepository productRepository;

    @KafkaListener(topics = "order-save-events", groupId = "product-service-group")
    public void consumeOrderSaveEvent(OrderSaveEvent event) {
        log.info("Received order-save-events from order service: {}", event);

        Product product = productRepository.findById(event.getProductId())
                .orElseThrow(() -> {
                    log.error("Product {} not found", event.getProductId());
                    return new ResourceNotFoundException("Product not found");
                });

        int updatedQuantity = product.getQuantity() - event.getQuantity();
        product.setQuantity(updatedQuantity);
        productRepository.save(product);

        log.info("Product {} quantity updated successfully to {}", product.getName(), updatedQuantity);
    }
}