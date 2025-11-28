package com.EIPplatform.controller.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.survey.CreateSurveyFormCategoryDTO;
import com.EIPplatform.model.dto.form.surveyform.survey.SurveyFormCategoryDTO;
import com.EIPplatform.service.form.surveyform.SurveyFormCategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/forms/categories")
@Validated
@RequiredArgsConstructor
public class SurveyFormCategoryController {
    private final SurveyFormCategoryService categoryService;

    @GetMapping("{id}")
    public ResponseEntity<SurveyFormCategoryDTO> getById(@PathVariable UUID id){
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SurveyFormCategoryDTO>> getAll(){
        return ResponseEntity.ok(categoryService.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity<List<SurveyFormCategoryDTO>> createCategoryList(@RequestBody @NotEmpty(message = "Please input at least ONE category.") List<@Valid CreateSurveyFormCategoryDTO> createSurveyFormCategoryDTOs) {
        List<SurveyFormCategoryDTO> createdCategories = categoryService.createCategoryList(createSurveyFormCategoryDTOs);
        return ResponseEntity.ok(createdCategories);
    }
}
