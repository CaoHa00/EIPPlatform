package com.EIPplatform.controller.form.submission;

import com.EIPplatform.model.dto.form.submission.AnswerDTO;
import com.EIPplatform.model.dto.form.submission.CreateAnswerDTO;
import com.EIPplatform.model.dto.form.submission.CreateSubmissionDTO;
import com.EIPplatform.model.dto.form.submission.SubmissionDTO;
import com.EIPplatform.service.form.submission.SubmissionServiceInterface;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/formsystem/submissions")
@RequiredArgsConstructor
@Validated
public class SubmissionController {
    private final SubmissionServiceInterface submissionService;

    @GetMapping("{submissionId}")

    public ResponseEntity<SubmissionDTO> getSubmissionById(@PathVariable UUID submissionId) {

        SubmissionDTO submissionDTO = submissionService.getSubmissionById(submissionId);

        return ResponseEntity.ok(submissionDTO);

    }

    // URL: GET /api/submissions?formId=...
    @GetMapping
    public ResponseEntity<List<SubmissionDTO>> getSubmissions(
            @RequestParam(required = false) UUID formId,
            @RequestParam(required = false) UUID respondentId) {

        if (formId != null) {
            return ResponseEntity.ok(submissionService.getSubmissionByFormId(formId));
        } else if (respondentId != null) {
            return ResponseEntity.ok(submissionService.getSubmissionByRespondentId(respondentId));
        }

        return ResponseEntity.ok(submissionService.getAllSubmissions()); // Or return all
    }

    /**
     * Create submission
     * Send formId and List of AnswerDTO entity
     * AnswerDTO list is nullable
     * @param createSubmissionDTO formId, nullable AnswerDTO list
     * @return SubmissionDTO
     */
    @PostMapping("/create")
    public ResponseEntity<SubmissionDTO> create(
            @RequestBody CreateSubmissionDTO createSubmissionDTO,
            @RequestParam(required = false) UUID userAccountId) {
        SubmissionDTO submissionDTO =  submissionService.createSubmission(createSubmissionDTO, userAccountId);
        return ResponseEntity.ok().body(submissionDTO);
    }

    /**
     * Granular endpoint to create answer of an existing Submission entity
     * @param submissionId UUID of the existing Submission
     * @param answerDTOList List of AnswerDTO entity (REQUIRED)
     * @return List of created Answers
     */
    @PutMapping("{submissionId}/answers/submit")
    public ResponseEntity<List<AnswerDTO>> submitAnswers(
            @PathVariable UUID submissionId,
            @RequestBody @NotEmpty(message = "Please input at least ONE answer.") List<@Valid CreateAnswerDTO> answerDTOList,
            @RequestParam(required = false) UUID userAccountId){
        List<AnswerDTO> dto = submissionService.submitAnswers(answerDTOList, submissionId, userAccountId);
        return ResponseEntity.ok(dto);
    }

    /**
     * Toggle the isDeleted boolean of Submission
     * @param submissionId submissionId
     * @return void
     */
    @DeleteMapping("{submissionId}/soft-delete")
    public ResponseEntity<SubmissionDTO> softDelete(
            @PathVariable UUID submissionId,
            @RequestParam(required = false) UUID userAccountId) {
        submissionService.softDeleteSubmission(submissionId, userAccountId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{submissionId}/hard-delete")
    public ResponseEntity<SubmissionDTO> hardDelete(
            @PathVariable UUID submissionId,
            @RequestParam(required = false) UUID userAccountId) {
        submissionService.hardDeleteSubmission(submissionId, userAccountId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/answers/{answerId}")
    public ResponseEntity<AnswerDTO> editAnswer(
            @PathVariable UUID answerId,
            @RequestParam @NotEmpty String value,
            @RequestParam(required = false) UUID userAccountId){
        AnswerDTO dto = submissionService.editAnswer(answerId, value, userAccountId);
        return ResponseEntity.ok(dto);
    }
}
