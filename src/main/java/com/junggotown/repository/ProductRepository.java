package com.junggotown.repository;

import com.junggotown.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<List<Product>> findByUserId(String userId);
    Product findByIdAndUserId(Long id, String userId);
}
