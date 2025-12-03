package com.EIPplatform.controller.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.ReorderRequestDTO;
import com.EIPplatform.model.dto.form.surveyform.question.CreateGroupDimensionDTO;
import com.EIPplatform.model.dto.form.surveyform.question.GroupDimensionDTO;
import com.EIPplatform.service.form.surveyform.GroupDimensionServiceInterface;
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
@RequestMapping("/api/formsystem/surveyforms/group-dimensions")
@Validated
@RequiredArgsConstructor
public class GroupDimensionController {

    private final GroupDimensionServiceInterface groupDimensionService;
    private final QuestionServiceInterface questionService;

    @GetMapping("{id}")
    public ResponseEntity<GroupDimensionDTO> getById(@PathVariable UUID id){
        return ResponseEntity.ok(groupDimensionService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<GroupDimensionDTO>> getAll(){
        return ResponseEntity.ok(groupDimensionService.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity<List<GroupDimensionDTO>> createGroupDimensionList(
            @RequestBody @NotEmpty(message = "Please input at least ONE group dimension.") List<@Valid CreateGroupDimensionDTO> createGroupDimensionDTOs,
            @RequestParam UUID dimensionId,
            @RequestParam UUID userAccountId) {
        List<GroupDimensionDTO> createdGroupDimensions = groupDimensionService.createGroupDimensionList(createGroupDimensionDTOs, dimensionId, userAccountId);
        return ResponseEntity.ok(createdGroupDimensions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupDimension(
            @PathVariable UUID id,
            @RequestParam UUID userAccountId) {
        groupDimensionService.deleteGroupDimension(id, userAccountId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupDimensionDTO> editGroupDimension(
            @PathVariable UUID id,
            @RequestBody @Valid CreateGroupDimensionDTO dto,
            @RequestParam UUID userAccountId){
        return ResponseEntity.ok(groupDimensionService.editGroupDimension(id, dto, userAccountId));
    }

    @PatchMapping("/{groupDimensionId}/questions/reorder")
    public ResponseEntity<Void> reorderQuestions(
            @PathVariable UUID groupDimensionId,
            @RequestBody @Valid ReorderRequestDTO dto,
            @RequestParam UUID userAccountId) {

        questionService.reorderQuestions(groupDimensionId, dto, userAccountId);
        return ResponseEntity.ok().build();
    }
}
