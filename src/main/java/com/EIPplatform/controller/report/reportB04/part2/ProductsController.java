package com.EIPplatform.controller.report.reportB04.part2;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.businessInformation.products.ProductCreationListRequest;
import com.EIPplatform.model.dto.businessInformation.products.ProductListDTO;
import com.EIPplatform.service.report.reportB04.part2.ReportB04Part2Service;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/v1/report-b04")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductsController {
    ReportB04Part2Service service;

    @PostMapping(value = "/{reportId}/draft/part-2")
    public ApiResponse<ProductListDTO> createDraftPart2(
            @PathVariable UUID reportId,
            @RequestParam UUID businessDetailId,
            @RequestBody @Valid ProductCreationListRequest request) {
        var result = service.createReportB04Part2(reportId, businessDetailId, request);
        return ApiResponse.<ProductListDTO>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{reportId}/initial/part-2")
    public ApiResponse<ProductListDTO> getDraftPart2(
            @PathVariable UUID reportId,
            @RequestParam UUID businessDetailId) {
        var result = service.getReportB04Part2(reportId, businessDetailId);
        return ApiResponse.<ProductListDTO>builder()
                .result(result)
                .build();
    }
}
