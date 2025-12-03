package com.EIPplatform.controller.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.ReorderRequestDTO;
import com.EIPplatform.model.dto.form.surveyform.survey.CreateDimensionDTO;
import com.EIPplatform.model.dto.form.surveyform.survey.DimensionDTO;
import com.EIPplatform.service.form.surveyform.DimensionServiceInterface;
import com.EIPplatform.service.form.surveyform.QuestionServiceInterface;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/formsystem/surveyforms/dimensions")
@Validated
@RequiredArgsConstructor
public class DimensionController {
    private final DimensionServiceInterface dimensionService;

    @GetMapping("{id}")
    public ResponseEntity<DimensionDTO> getById(@PathVariable UUID id){
        return ResponseEntity.ok(dimensionService.getById(id));
    }

    /**
     * Get all Dimensions of a SurveyForm. Return all Dimensions if SurveyFormId not provided
     * @param surveyFormId surveyFormId
     * @return
     */
    @GetMapping
    public ResponseEntity<List<DimensionDTO>> getAll(@RequestParam(required = false) UUID surveyFormId){
        if (surveyFormId != null) {
            return ResponseEntity.ok(dimensionService.getAllBySurveyFormId(surveyFormId));
        }
        return ResponseEntity.ok(dimensionService.getAll());
    }


    @PostMapping("/create")
    public ResponseEntity<List<DimensionDTO>> createDimensionList(
            @RequestBody @NotEmpty(message = "Please input at least ONE dimension.") List<@Valid CreateDimensionDTO> createDimensionDTOs,
            @RequestParam UUID surveyFormId,
            @RequestParam UUID userAccountId) {
        List<DimensionDTO> createdDimensions = dimensionService.createDimensionList(createDimensionDTOs, surveyFormId, userAccountId);
        return ResponseEntity.ok(createdDimensions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDimension(
            @PathVariable UUID id,
            @RequestParam UUID userAccountId) {
        dimensionService.deleteDimension(id, userAccountId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DimensionDTO> editDimensionName(
            @PathVariable UUID id,
            @RequestParam String name,
            @RequestParam UUID userAccountId){
        return ResponseEntity.ok(dimensionService.editDimensionName(id, name, userAccountId));
    }
}
