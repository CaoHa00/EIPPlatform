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

    @PostMapping(value="/{reportId}/draft/resources-saving-and-reduction")
    public ApiResponse<ResourcesSavingAndReductionDTO> createResourcesSavingAndReduction (
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId,
            @RequestPart("data") @Valid ResourcesSavingAndReductionCreateRequestDTO request
    ) {
        var result = resourcesSavingAndReductionService.createResourcesSavingAndReduction(reportId, userAccountId, request);
        return ApiResponse.<ResourcesSavingAndReductionDTO>builder()
                .result(result)
                .build();
    }
    @GetMapping(value="/{reportId}/draft/resources-saving-and-reduction")
    public ApiResponse<ResourcesSavingAndReductionDTO> getResourcesSavingAndReduction (
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId
    ) {
        var result = resourcesSavingAndReductionService.getResourcesSavingAndReduction(reportId, userAccountId);
        return ApiResponse.<ResourcesSavingAndReductionDTO>builder()
                .result(result)
                .build();
    }
    @DeleteMapping(value="/{reportId}/draft/resources-saving-and-reduction")
    public ApiResponse<Void> deleteResourcesSavingAndReduction (
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId
    ) {
      resourcesSavingAndReductionService.deleteResourcesSavingAndReduction(reportId, userAccountId);
      return ApiResponse.<Void>builder()
              .build();
    }
}
