package com.EIPplatform.model.dto.form.surveyform;

import com.EIPplatform.model.dto.form.submission.AnswerDTO;
import com.EIPplatform.model.dto.form.surveyform.question.QuestionDTO;
import lombok.Data;

import java.util.List;

@Data
public class QuestionsAndAnswersDTO {
    List<QuestionDTO> questions;
    List<AnswerDTO> inputAnswers;
    List<AnswerDTO> comparisonAnswers;
}
