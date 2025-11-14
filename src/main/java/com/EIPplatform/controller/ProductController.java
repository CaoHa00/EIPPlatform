package com.EIPplatform.controller;



import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.products.ProductCreationRequest;
import com.EIPplatform.model.dto.products.ProductResponse;
import com.EIPplatform.model.dto.products.ProductUpdateRequest;
import com.EIPplatform.service.products.ProductInterface;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductInterface productService;

    @PostMapping
    public ApiResponse<ProductResponse> createProduct(
            @RequestBody @Valid ProductCreationRequest request) {

        ProductResponse response = productService.createProduct(request);

        return ApiResponse.<ProductResponse>builder()
                .message("Product created successfully")
                .result(response)
                .build();
    }

    @PutMapping
    public ApiResponse<ProductResponse> updateProduct(
            @RequestBody @Valid ProductUpdateRequest request) {

        ProductResponse response = productService.updateProduct(request);

        return ApiResponse.<ProductResponse>builder()
                .message("Product updated successfully")
                .result(response)
                .build();
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProductById(
            @PathVariable UUID productId) {

        ProductResponse response = productService.getProductById(productId);

        return ApiResponse.<ProductResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAllProducts() {

        List<ProductResponse> response = productService.getAllProducts();

        return ApiResponse.<List<ProductResponse>>builder()
                .result(response)
                .build();
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<Void> deleteProduct(
            @PathVariable UUID productId) {

        productService.deleteProduct(productId);

        return ApiResponse.<Void>builder()
                .message("Product deleted successfully")
                .build();
    }
}