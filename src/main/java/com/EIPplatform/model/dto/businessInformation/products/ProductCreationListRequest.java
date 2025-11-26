package com.EIPplatform.model.dto.businessInformation.products;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreationListRequest {

    @Valid
    @NotEmpty(message = "FIELD_REQUIRED")
    List<ProductCreationRequest> products;

    @NotNull(message = "FIELD_REQUIRED")
    UUID businessDetailId;
}
