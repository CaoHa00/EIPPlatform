package com.EIPplatform.model.dto.form.surveyform.question;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.EIPplatform.model.dto.form.surveyform.option.CreateOptionDTO;

import java.util.List;
import java.util.UUID;

@Data
public class CreateQuestionDTO {
    @NotBlank(message = "Question text is required")
    private String text;

    @NotBlank(message = "Question code is required")
    private String code;

    @NotBlank(message = "Type is required")
    private String type;

    @NotNull(message = "Display Order is required")
    @Min(value = 1, message = "displayOrder value must be greater than 1.")
    private Integer displayOrder;

    private Boolean required;

    @NotNull(message = "Group Dimension ID is required")
    private UUID groupDimensionId;

    @NotNull(message = "Input Business ID is required")
    private UUID inputBusinessId;

    @NotNull(message = "Comparison Business ID is required")
    private UUID comparisonBusinessId;

    @Valid
    private List<CreateOptionDTO> options;
}
