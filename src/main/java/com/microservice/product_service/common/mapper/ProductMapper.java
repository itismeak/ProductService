package com.microservice.product_service.common.mapper;

import com.microservice.product_service.common.dto.ProductRequestDto;
import com.microservice.product_service.common.dto.ProductViewDto;
import com.microservice.product_service.module.product.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductMapper {
    private final ModelMapper modelMapper;

    public ProductMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    public Product toProduct(ProductRequestDto requestDto){
        Product product=new Product();
        modelMapper.map(requestDto,product);
        return product;
    }
    public ProductViewDto productViewDto(Product product){
        return  modelMapper.map(product,ProductViewDto.class);
    }
}
