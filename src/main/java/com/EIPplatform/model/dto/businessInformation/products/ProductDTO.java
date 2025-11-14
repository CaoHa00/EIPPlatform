package com.EIPplatform.model.dto.businessInformation.products;

import java.util.UUID;

import com.EIPplatform.model.entity.businessInformation.BusinessDetail;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {

    UUID productId;
    String productName;
    String productUnit;
    String productDesignedCapacity;
    String productActualOutput;
    String productEnergyConsumedSources;
    String productImage;
    BusinessDetail businessDetail;

}
