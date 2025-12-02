package com.EIPplatform.model.dto.businessInformation.products;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

    UUID productId;
    String productName;
    String productUnit;
    String productDesignedCapacity;
    String productActualOutput;
    String productEnergyConsumedSources;
    String productImage;

}