package com.microservice.product_service.module.product.service;

import com.microservice.product_service.common.dto.ProductRequestDto;
import com.microservice.product_service.common.dto.ProductViewDto;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductViewDto saveProduct(ProductRequestDto dto);
    ProductViewDto getProduct(Long id);
    ProductViewDto updateProduct(Long id,ProductRequestDto dto);
    Page<ProductViewDto> getAllProducts(int page, int size,
                              String search);
}
