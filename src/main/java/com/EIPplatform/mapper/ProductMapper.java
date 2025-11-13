package com.EIPplatform.mapper;


import org.mapstruct.*;

import com.EIPplatform.model.dto.products.ProductCreationRequest;
import com.EIPplatform.model.dto.products.ProductResponse;
import com.EIPplatform.model.dto.products.ProductUpdateRequest;
import com.EIPplatform.model.entity.products.Product;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    Product toEntity(ProductCreationRequest request);

    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ProductUpdateRequest request, @MappingTarget Product entity);

    ProductResponse toResponse(Product entity);
}