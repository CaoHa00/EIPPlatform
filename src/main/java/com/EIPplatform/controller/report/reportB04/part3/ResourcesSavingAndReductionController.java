package com.EIPplatform.controller.report.reportB04.part3;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.report.reportB04.part3.ResourcesSavingAndReductionDTO;
import com.EIPplatform.model.dto.report.reportB04.part3.request.ResourcesSavingAndReductionCreateRequestDTO;
import com.EIPplatform.service.report.reportB04.part3.ResourcesSavingAndReductionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports-b04")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ResourcesSavingAndReductionController {
    ResourcesSavingAndReductionService resourcesSavingAndReductionService;

    @PostMapping(value="/{reportId}/draft/part-3")
    public ApiResponse<ResourcesSavingAndReductionDTO> createReportB04Part3(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId,
            @RequestPart("data") @Valid ResourcesSavingAndReductionCreateRequestDTO request
    ) {
        var result = resourcesSavingAndReductionService.createReportB04Part3(reportId, userAccountId, request);
        return ApiResponse.<ResourcesSavingAndReductionDTO>builder()
                .result(result)
                .build();
    }
    @GetMapping(value="/{reportId}/draft/part-3")
    public ApiResponse<ResourcesSavingAndReductionDTO> getReportB04Part3(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId
    ) {
        var result = resourcesSavingAndReductionService.getReportB04Part3(reportId, userAccountId);
        return ApiResponse.<ResourcesSavingAndReductionDTO>builder()
                .result(result)
                .build();
    }
    @DeleteMapping(value="/{reportId}/draft/part-3")
    public ApiResponse<Void> deleteReportB04Part3(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId
    ) {
      resourcesSavingAndReductionService.deleteReportB04Part3(reportId, userAccountId);
      return ApiResponse.<Void>builder()
              .build();
    }
}
