package com.EIPplatform.controller.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.question.CreateQuestionCategoryDTO;
import com.EIPplatform.model.dto.form.surveyform.question.QuestionCategoryDTO;
import com.EIPplatform.service.form.surveyform.QuestionCategoryServiceInterface;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/forms/question-categories")
@Validated
@RequiredArgsConstructor
public class QuestionCategoryController {

    private final QuestionCategoryServiceInterface questionCategoryService;

    @GetMapping("{id}")
    public ResponseEntity<QuestionCategoryDTO> getById(@PathVariable UUID id){
        return ResponseEntity.ok(questionCategoryService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<QuestionCategoryDTO>> getAll(){
        return ResponseEntity.ok(questionCategoryService.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity<List<QuestionCategoryDTO>> createCategoryList(@RequestBody @NotEmpty(message = "Please input at least ONE category.") List<@Valid CreateQuestionCategoryDTO> createQuestionCategoryDTOs,
                                                                        @RequestParam UUID surveyFormCategoryId) {
        List<QuestionCategoryDTO> createdCategories = questionCategoryService.createCategoryList(createQuestionCategoryDTOs, surveyFormCategoryId);
        return ResponseEntity.ok(createdCategories);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId) {
        questionCategoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<QuestionCategoryDTO> editCategory(@PathVariable UUID categoryId, @RequestBody @Valid CreateQuestionCategoryDTO dto){
        return ResponseEntity.ok(questionCategoryService.editCategory(categoryId, dto));
    }
}
