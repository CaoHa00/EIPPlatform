package com.EIPplatform.model.dto.form.surveyform.option;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOptionDTO {
    @NotBlank(message = "Option text is required")
    private String text;

    @NotNull(message = "Display Order is required")
    @Min(value = 1, message = "displayOrder value must be greater than 1.")
    private Integer displayOrder;
}
