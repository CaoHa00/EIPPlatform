package com.EIPplatform.model.dto.form.surveyform.question;

import jakarta.validation.Valid;
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

    @NotBlank(message = "Type is required")
    private String type;

    @NotNull(message = "Display Order is required")
    private Integer displayOrder;

    private Boolean required;

    @NotBlank(message = "CategoryId is required")
    private UUID categoryId;

    @Valid
    private List<CreateOptionDTO> options;
}
