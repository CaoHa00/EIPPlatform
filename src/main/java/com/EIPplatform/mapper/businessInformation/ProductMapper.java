package com.EIPplatform.mapper.businessInformation;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.EIPplatform.model.dto.businessInformation.products.ProductCreationRequest;
import com.EIPplatform.model.dto.businessInformation.products.ProductDTO;
import com.EIPplatform.model.dto.businessInformation.products.ProductListDTO;
import com.EIPplatform.model.dto.businessInformation.products.ProductResponse;
import com.EIPplatform.model.dto.businessInformation.products.ProductUpdateRequest;
import com.EIPplatform.model.entity.businessInformation.products.Product;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    @Mapping(target = "reportB04", ignore = true)
    Product toEntity(ProductResponse request);

    @Named("fromCreate")
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    @Mapping (target = "reportB04", ignore = true)
    Product toEntityFromCreate(ProductCreationRequest request);

    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    @Mapping(target = "reportB04", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ProductUpdateRequest request, @MappingTarget Product entity);

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    @Mapping(target = "reportB04", ignore = true)
    Product dtoToEntity(ProductDTO dto);

    ProductResponse toResponse(Product entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    @Mapping(target = "reportB04", ignore = true)
    void updateEntityFromDto(ProductResponse dto, @MappingTarget Product entity);

    ProductDTO toDTO(Product entity);

    List<Product> toEntityList(List<ProductResponse> dtos);

    @IterableMapping(qualifiedByName = "fromCreate")

    List<Product> toEntityListFromCreate(List<ProductCreationRequest> dtos);

    @Named("toDTOFromCreate")
    @Mapping(target = "productId", ignore = true)
    ProductResponse toDTOFromCreate(Product entity);

    // Map list → list
    @IterableMapping(qualifiedByName = "toDTOFromCreate")
    List<ProductResponse> toDTOListFromCreate(List<Product> entities);

    default ProductListDTO toDTOList(List<Product> entities) {
        return ProductListDTO.builder()
                .products(toDTOListFromCreate(entities))
                .build();
    }
}
