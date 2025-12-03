package com.EIPplatform.controller.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.ReorderRequestDTO;
import com.EIPplatform.model.dto.form.surveyform.survey.CreateSurveyFormDTO;
import com.EIPplatform.model.dto.form.surveyform.survey.EditSurveyDTO;
import com.EIPplatform.model.dto.form.surveyform.survey.ExpiryRequest;
import com.EIPplatform.model.dto.form.surveyform.survey.SurveyDTO;
import com.EIPplatform.service.form.surveyform.QuestionService;
import com.EIPplatform.service.form.surveyform.SurveyService;
import com.EIPplatform.service.form.surveyform.SurveyServiceInterface;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/formsystem/surveyforms")
public class SurveyController {

    private final SurveyServiceInterface surveyService;

    @GetMapping
    public ResponseEntity<List<SurveyDTO>> Get(
            @RequestParam(required = false) UUID creatorId,
            @RequestParam(required = false) String title
    ) {
        if (creatorId != null || title != null) {
            return ResponseEntity.ok(surveyService.searchSurveys(creatorId, title));
        }

        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @PostMapping("/create")
    public ResponseEntity<SurveyDTO> createSurvey(
            @Valid @RequestBody CreateSurveyFormDTO dto,
            @RequestParam UUID userAccountId) {
        SurveyDTO createdSurvey = surveyService.createSurvey(dto, userAccountId);
        return ResponseEntity.ok(createdSurvey);
    }

    @PatchMapping("/{id}/edit")
    public ResponseEntity<SurveyDTO> editSurvey(
            @PathVariable UUID id,
            @RequestBody EditSurveyDTO dto,
            @RequestParam UUID userAccountId) {
        SurveyDTO editSurvey = surveyService.editSurvey(id, dto, userAccountId);
        return ResponseEntity.ok(editSurvey);
    }

    //this is basically soft delete
    @PatchMapping("{id}/active-switch")
    public ResponseEntity<Void> activeSwitch(
            @PathVariable UUID id,
            @RequestParam UUID userAccountId) {
        surveyService.activeSwitch(id, userAccountId);
        return ResponseEntity.ok().build();
    }

    //To Set: Send { "expiresAt": "2025-12-31T23:59:59" }
    //To Clear: Send { "expiresAt": null }
    @PatchMapping("/{id}/expire")
    public ResponseEntity<SurveyDTO> updateExpiry(
            @PathVariable UUID id,
            @RequestBody ExpiryRequest request,
            @RequestParam UUID userAccountId) {
        return ResponseEntity.ok(surveyService.updateExpiry(id, request.getExpiresAt(), userAccountId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDTO> getSurvey(@PathVariable UUID id) {
        return ResponseEntity.ok(surveyService.getSurveyDTO(id));
    }

    @DeleteMapping("/{id}/hard-delete")
    public ResponseEntity<Void> hardDeleteSurvey(
            @PathVariable UUID id,
            @RequestParam UUID userAccountId) {
        surveyService.hardDeleteSurvey(id, userAccountId);
        return ResponseEntity.noContent().build();
    }
}
