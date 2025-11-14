package com.microservice.product_service.module.product.serviceImp;

import com.microservice.product_service.common.dto.ProductRequestDto;
import com.microservice.product_service.common.dto.ProductViewDto;
import com.microservice.product_service.common.mapper.ProductMapper;
import com.microservice.product_service.module.product.entity.Product;
import com.microservice.product_service.module.product.repository.ProductRepository;
import com.microservice.product_service.module.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductViewDto saveProduct(ProductRequestDto dto) {
        if(productRepository.existsByName(dto.getName())){
            throw new RuntimeException("Product '"+dto.getName()+"' already exists");
        }
        Product product = productMapper.toProduct(dto);
        Product savedProduct = productRepository.save(product);
        return productMapper.productViewDto(savedProduct);
    }

    @Override
    public ProductViewDto getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.productViewDto(product);
    }

    @Override
    public ProductViewDto updateProduct(Long id, ProductRequestDto dto) {
        if (productRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Product '" + dto.getName() + "' already exists");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update product fields with data from DTO
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setQuantity(dto.getQuantity());
        product.setPrice(dto.getPrice());
        product.setStatus(dto.getStatus());
        product.setCategory(dto.getCategory());

        // Log the update
        log.info("Updating product with id {}: {}", id, dto);

        Product updatedProduct = productRepository.save(product);
        return productMapper.productViewDto(updatedProduct);
    }

    @Override
    public Page<ProductViewDto> getAllProducts(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);

        // Search by product name if provided
        Page<Product> productPage;
        if (search != null && !search.isEmpty()) {
            log.info("Fetching products with search query: '{}'", search);
            productPage = productRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            log.info("Fetching all products without search filter");
            productPage = productRepository.findAll(pageable);
        }

        // Map Product entities to ProductViewDtos
        return productPage.map(productMapper::productViewDto);
    }
}