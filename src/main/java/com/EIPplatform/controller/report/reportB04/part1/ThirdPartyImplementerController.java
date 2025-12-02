package com.EIPplatform.controller.report.reportB04.part1;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ThirdPartyImplementerCreationRequest;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ThirdPartyImplementerUpdateRequest;
import com.EIPplatform.model.dto.report.reportB04.part1.response.ThirdPartyImplementerResponse;
import com.EIPplatform.service.report.reportB04.part1.ThirdPartyImplementerService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/v1/third-party")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ThirdPartyImplementerController {

    ThirdPartyImplementerService service;

    @PostMapping
    public ApiResponse<ThirdPartyImplementerResponse> create(@RequestBody @Valid ThirdPartyImplementerCreationRequest request) {
        ThirdPartyImplementerResponse response = service.create(request);
        return ApiResponse.<ThirdPartyImplementerResponse>builder()
                .message("Created successfully")
                .result(response)
                .build();
    }

    @PutMapping
    public ApiResponse<ThirdPartyImplementerResponse> update(@RequestBody @Valid ThirdPartyImplementerUpdateRequest request) {
        ThirdPartyImplementerResponse response = service.update(request);
        return ApiResponse.<ThirdPartyImplementerResponse>builder()
                .message("Updated successfully")
                .result(response)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ThirdPartyImplementerResponse> getById(@PathVariable Long id) {
        ThirdPartyImplementerResponse response = service.getById(id);
        return ApiResponse.<ThirdPartyImplementerResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping
    public ApiResponse<List<ThirdPartyImplementerResponse>> getAll() {
        List<ThirdPartyImplementerResponse> response = service.getAll();
        return ApiResponse.<List<ThirdPartyImplementerResponse>>builder()
                .result(response)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }
}