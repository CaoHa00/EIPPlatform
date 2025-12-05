package com.EIPplatform.model.dto.form.surveyform.question;

import com.EIPplatform.model.dto.form.surveyform.option.OptionDTO;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class QuestionDTO {
    private UUID id;
    private String code;
    private String text;
    private String type;
    private boolean required;
    private int displayOrder;
    private boolean active;
    private UUID comparisonBusinessId;
    private UUID inputBusinessId;
    private UUID groupDimensionId;
    private String groupDimensionName;

    private List<OptionDTO> options;
}
