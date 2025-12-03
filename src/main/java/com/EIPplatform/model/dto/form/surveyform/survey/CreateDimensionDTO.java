package com.EIPplatform.model.dto.form.surveyform.survey;

import com.EIPplatform.model.dto.form.surveyform.question.CreateGroupDimensionDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CreateDimensionDTO {
    @NotBlank(message = "Dimension name is required")
    private String name;

    @Valid
    private Set<CreateGroupDimensionDTO> groupDimensions = new HashSet<>();

}
