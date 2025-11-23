package com.EIPplatform.mapper.businessInformation;


import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.EIPplatform.model.dto.businessInformation.products.ProductCreationListRequest;
import com.EIPplatform.model.dto.businessInformation.products.ProductCreationRequest;
import com.EIPplatform.model.dto.businessInformation.products.ProductDTO;
import com.EIPplatform.model.dto.businessInformation.products.ProductResponse;
import com.EIPplatform.model.dto.businessInformation.products.ProductUpdateRequest;
import com.EIPplatform.model.entity.businessInformation.products.Product;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    @Mapping (target = "reportB04", ignore = true)
    Product toEntity(ProductCreationRequest request);

    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    @Mapping (target = "reportB04", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ProductUpdateRequest request, @MappingTarget Product entity);

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    @Mapping (target = "reportB04", ignore = true)
    Product dtoToEntity(ProductDTO dto);

    ProductResponse toResponse(Product entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    @Mapping (target = "reportB04", ignore = true)
    void updateEntityFromDto (ProductCreationRequest dto, @MappingTarget Product entity);

    ProductDTO toDTO (Product entity);

    List<Product> toEntityList(List<ProductCreationRequest> dtos);

     List<ProductCreationRequest> toDTOList(List<Product> entities);
}