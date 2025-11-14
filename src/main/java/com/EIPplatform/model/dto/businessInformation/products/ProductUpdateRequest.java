package com.EIPplatform.model.dto.businessInformation.products;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {

    @NotNull(message = "FIELD_REQUIRED")
    UUID productId;

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