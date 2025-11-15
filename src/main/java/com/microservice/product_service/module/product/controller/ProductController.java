package com.microservice.product_service.module.product.controller;

import com.microservice.product_service.common.constant.AppConstant;
import com.microservice.product_service.common.dto.ApiResponse;
import com.microservice.product_service.common.dto.ProductRequestDto;
import com.microservice.product_service.common.dto.ProductViewDto;
import com.microservice.product_service.module.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.apiVersion +"/product")
public class ProductController {
    private final ProductService productService;
    // Create a new product
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<ProductViewDto>> createProduct(@RequestBody ProductRequestDto dto) {
        log.info("Received request to create product: {}", dto.toString());

        ProductViewDto productViewDto = productService.saveProduct(dto);

        log.info("Product created successfully with ID: {}", productViewDto.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Product created successfully",productViewDto,true));
    }

    // Get a product by ID
    @GetMapping("/GetById/{id}")
    public ResponseEntity<ApiResponse<ProductViewDto>> getProduct(@PathVariable Long id) {
        log.info("Received request to get product with ID: {}", id);

        ProductViewDto productViewDto = productService.getProduct(id);

        log.info("Product found: {}", productViewDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Product fetch successfully",productViewDto,true));
    }

    // Update an existing product
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<ProductViewDto>> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDto dto) {
        log.info("Received request to update product with ID: {}. New data: {}", id, dto);
        ProductViewDto updatedProduct = productService.updateProduct(id, dto);
        log.info("Product updated successfully with ID: {}", updatedProduct.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Product updated successfully",
                        updatedProduct,true));
    }

    // Get a paginated list of all products
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<Page<ProductViewDto>>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "search", defaultValue = "") String search) {

        log.info("Received request to get products with page: {}, size: {}, search: {}", page, size, search);

        Page<ProductViewDto> products = productService.getAllProducts(page, size, search);

        log.info("Fetched {} products from database", products.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Fetched product successfull",products,true));
    }
}
