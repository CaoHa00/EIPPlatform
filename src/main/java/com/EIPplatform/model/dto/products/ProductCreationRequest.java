package com.EIPplatform.model.dto.products;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreationRequest {

    @NotBlank(message = "FIELD_REQUIRED")
    String productName;

    @NotBlank(message = "FIELD_REQUIRED")
    String productUnit;

    @NotBlank(message = "FIELD_REQUIRED")
    String productDesignedCapacity;

    @NotBlank(message = "FIELD_REQUIRED")
    String productActualOutput;

    @NotBlank(message = "FIELD_REQUIRED")
    String productEnergyConsumedSources;

    String productImage;
}