package com.EIPplatform.service.products;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ProductError;
import com.EIPplatform.mapper.ProductMapper;
import com.EIPplatform.model.dto.products.ProductCreationRequest;
import com.EIPplatform.model.dto.products.ProductResponse;
import com.EIPplatform.model.dto.products.ProductUpdateRequest;
import com.EIPplatform.model.entity.products.Product;
import com.EIPplatform.repository.user.ProductRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductImplementation implements ProductInterface {

    ProductRepository productRepository;
    ProductMapper productMapper;
    ExceptionFactory exceptionFactory;

    @Override
    public ProductResponse createProduct(ProductCreationRequest request) {

        // Check if product name already exists
        if (productRepository.existsByProductName(request.getProductName())) {
            throw exceptionFactory.createAlreadyExistsException(
                    "Product",
                    "product name",
                    request.getProductName(),
                    ProductError.PRODUCT_NAME_ALREADY_EXISTS);
        }

        Product entity = productMapper.toEntity(request);
        Product savedEntity = productRepository.save(entity);
        return productMapper.toResponse(savedEntity);
    }

    @Override
    public ProductResponse updateProduct(ProductUpdateRequest request) {

        // Find existing product
        Product existingEntity = productRepository
                .findById(request.getProductId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Product",
                        request.getProductId(),
                        ProductError.PRODUCT_NOT_FOUND));

        // Check if product name is being changed and if new name already exists
        if (productRepository.existsByProductNameAndProductIdNot(
                request.getProductName(),
                request.getProductId())) {
            throw exceptionFactory.createAlreadyExistsException(
                    "Product",
                    "product name",
                    request.getProductName(),
                    ProductError.PRODUCT_NAME_ALREADY_EXISTS);
        }

        productMapper.updateEntityFromRequest(request, existingEntity);
        Product updatedEntity = productRepository.save(existingEntity);
        return productMapper.toResponse(updatedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID productId) {

        Product entity = productRepository
                .findById(productId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Product",
                        productId,
                        ProductError.PRODUCT_NOT_FOUND));

        return productMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(UUID productId) {

        if (!productRepository.existsById(productId)) {
            throw exceptionFactory.createNotFoundException(
                    "Product",
                    productId,
                    ProductError.PRODUCT_NOT_FOUND);
        }

        productRepository.deleteById(productId);
    }
}
