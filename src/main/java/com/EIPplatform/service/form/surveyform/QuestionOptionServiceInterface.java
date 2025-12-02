package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.ReorderRequestDTO;
import com.EIPplatform.model.dto.form.surveyform.option.CreateOptionDTO;
import com.EIPplatform.model.dto.form.surveyform.option.OptionDTO;
import com.EIPplatform.model.entity.form.surveyform.Question;
import com.EIPplatform.model.entity.form.surveyform.QuestionOption;

import java.util.UUID;

public interface QuestionOptionServiceInterface {
    OptionDTO getOption(UUID id);
    QuestionOption buildOptionEntity(CreateOptionDTO dto, Question parent);
    OptionDTO addOption(UUID questionId, CreateOptionDTO dto);
    OptionDTO editOption(String text, UUID optionId);
    void activeSwitch(UUID optionId);
    void reorderOptions(UUID questionId, ReorderRequestDTO dto);
    void batchDeleteOptions(Question question);
    void hardDeleteOption(UUID optionId);
}
