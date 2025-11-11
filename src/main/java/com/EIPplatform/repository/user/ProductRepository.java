package com.EIPplatform.repository.user;

import com.example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    
    Optional<Product> findByProductName(String productName);
    
    boolean existsByProductName(String productName);
    
    boolean existsByProductNameAndProductIdNot(String productName, UUID productId);
}