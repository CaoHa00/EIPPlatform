package com.EIPplatform.model.dto.businessInformation.products;

import jakarta.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

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

    @NotBlank(message = "FIELD_REQUIRED")
    UUID businessDetailId;

}