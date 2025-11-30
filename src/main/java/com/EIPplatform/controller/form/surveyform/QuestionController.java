package com.EIPplatform.controller.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.ReorderRequestDTO;
import com.EIPplatform.model.dto.form.surveyform.question.CreateQuestionDTO;
import com.EIPplatform.model.dto.form.surveyform.question.EditQuestionDTO;
import com.EIPplatform.model.dto.form.surveyform.question.QuestionDTO;
import com.EIPplatform.service.form.surveyform.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final QuestionOptionService questionOptionService;

    public QuestionController(QuestionService questionService, QuestionOptionService questionOptionService) {
        this.questionService = questionService;
        this.questionOptionService = questionOptionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable UUID id) {
        QuestionDTO question = questionService.getQuestion(id);
        return ResponseEntity.ok(question);
    }

    //example
//    {
//        "text": "Write sth idk",
//            "type": "SHORT_ANSWER",
//            "displayOrder": "5"
//    }
    @PostMapping("/create")
    public ResponseEntity<QuestionDTO> createQuestion(
            @RequestParam UUID formId,
            @Valid @RequestBody CreateQuestionDTO dto) {

        QuestionDTO createdQuestion = questionService.addQuestion(dto, formId);

        return ResponseEntity.ok(createdQuestion);
    }

    @DeleteMapping("/{id}/hard-delete")
    public ResponseEntity<Void> deleteQuestion(@PathVariable UUID id) {
        questionService.hardDeleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    //example: PATCH /api/questions/{id} -> { "text": "What is your corrected name?" }
    //example 2: PATCH /api/questions/{id} -> { "required": true }
    @PatchMapping("/{id}/edit")
    public ResponseEntity<QuestionDTO> editQuestion(
            @PathVariable UUID id,
            @RequestBody EditQuestionDTO dto) throws IllegalAccessException {

        QuestionDTO updatedQuestion = questionService.editQuestion(id, dto);
        return ResponseEntity.ok(updatedQuestion);
    }

    @PatchMapping("/{id}/active-switch")
    public ResponseEntity<Void> activeSwitchQuestion(@PathVariable UUID id) {
        questionService.activeSwitch(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/options/reorder")
    public ResponseEntity<Void> reorderOptions(
            @PathVariable("id") UUID questionId,
            @RequestBody @Valid ReorderRequestDTO dto) {

        questionOptionService.reorderOptions(questionId, dto);
        return ResponseEntity.ok().build();
    }
}
