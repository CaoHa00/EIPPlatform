package com.EIPplatform.service.products;

import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.products.ProductCreationRequest;
import com.EIPplatform.model.dto.products.ProductResponse;
import com.EIPplatform.model.dto.products.ProductUpdateRequest;

public interface ProductInterface {
    ProductResponse createProduct(ProductCreationRequest request);

    ProductResponse updateProduct(ProductUpdateRequest request);

    ProductResponse getProductById(UUID productId);

    List<ProductResponse> getAllProducts();

    void deleteProduct(UUID productId);
}