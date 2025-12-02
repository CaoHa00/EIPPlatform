package com.EIPplatform.repository.businessInformation;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.businessInformation.products.Product;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByProductName(String productName);

    boolean existsByProductName(String productName);

    boolean existsByProductNameAndProductIdNot(String productName, UUID productId);

    @Query("""
                SELECT p
                FROM Product p
                WHERE p.businessDetail.businessDetailId = :businessDetailId
            """)
    Optional<Product> findByBusinessDetailId(@Param("businessDetailId") UUID businessDetailId);
}
