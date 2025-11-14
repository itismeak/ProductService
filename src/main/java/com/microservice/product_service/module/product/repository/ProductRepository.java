package com.microservice.product_service.module.product.repository;

import com.microservice.product_service.module.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByName(String name);

    Page<Product> findByNameContainingIgnoreCase(String search, Pageable pageable);
}