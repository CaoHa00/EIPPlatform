package com.EIPplatform.model.dto.form.surveyform.survey;

import com.EIPplatform.model.dto.form.surveyform.question.GroupDimensionDTO;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class DimensionDTO {
    private UUID id;
    private String name;
    private Set<GroupDimensionDTO> groupDimensions;
}
