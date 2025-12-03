package com.EIPplatform.controller.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.option.CreateOptionDTO;
import com.EIPplatform.model.dto.form.surveyform.option.OptionDTO;
import com.EIPplatform.service.form.surveyform.QuestionOptionService;
import com.EIPplatform.service.form.surveyform.QuestionOptionServiceInterface;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/formsystem/surveyforms/questions/options")
public class QuestionOptionController {
    private final QuestionOptionServiceInterface questionOptionService;

    public QuestionOptionController(QuestionOptionService questionOptionService) {
        this.questionOptionService = questionOptionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OptionDTO> getOption(@PathVariable UUID id) {
        OptionDTO option = questionOptionService.getOption(id);
        return ResponseEntity.ok(option);
    }

    @PostMapping("/create")
    public ResponseEntity<OptionDTO> createQuestion(
            @RequestParam UUID questionId,
            @Valid @RequestBody CreateOptionDTO dto,
            @RequestParam UUID userAccountId) {

        OptionDTO createdOption = questionOptionService.addOption(questionId, dto, userAccountId);

        return ResponseEntity.ok(createdOption);
    }

    @DeleteMapping("/{id}/hard-delete")
    public ResponseEntity<Void> deleteOption(
            @PathVariable UUID id,
            @RequestParam UUID userAccountId) {
        questionOptionService.hardDeleteOption(id, userAccountId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/edit")
    public ResponseEntity<OptionDTO> editOption(
            @PathVariable UUID id,
            @RequestParam String text,
            @RequestParam UUID userAccountId) throws IllegalAccessException {
        return ResponseEntity.ok(questionOptionService.editOption(text, id, userAccountId));
    }

    @PatchMapping("/{id}/active-switch")
    public ResponseEntity<Void> toggleActive(
            @PathVariable UUID id,
            @RequestParam UUID userAccountId) {
        questionOptionService.activeSwitch(id, userAccountId);
        return ResponseEntity.ok().build();
    }
}
