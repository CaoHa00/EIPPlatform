package com.EIPplatform.controller.report.report06;
import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.report.report06.CreateReportRequest;
import com.EIPplatform.model.dto.report.report06.Report06DTO;
import com.EIPplatform.model.dto.report.report06.Report06DraftDTO;
import com.EIPplatform.service.report.report06.Report06Service;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/report06")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class Report06Controller {

    Report06Service report06Service;

    /**
     * Tạo mới một báo cáo Report06 (chỉ tạo bản ghi chính + mã báo cáo)
     */
    @PostMapping
    public ApiResponse<Report06DTO> createReport(@RequestBody @Valid CreateReportRequest request) {
        Report06DTO result = report06Service.createReport(request);
        return ApiResponse.<Report06DTO>builder()
                .result(result)
                .build();
    }

    /**
     * Lấy thông tin báo cáo Report06 đã lưu trong DB (không phải draft)
     */
    @GetMapping("/{report06Id}")
    public ApiResponse<Report06DTO> getReportById(@PathVariable UUID report06Id) {
        Report06DTO result = report06Service.getReportById(report06Id);
        return ApiResponse.<Report06DTO>builder()
                .result(result)
                .build();
    }

    /**
     * Lấy dữ liệu draft (từ Redis) của toàn bộ Report06
     */
    @GetMapping("/{report06Id}/draft")
    public ApiResponse<Report06DraftDTO> getDraftData(
            @PathVariable UUID report06Id,
            @RequestParam UUID userAccountId) {

        Report06DraftDTO draft = report06Service.getDraftData(report06Id, userAccountId);
        return ApiResponse.<Report06DraftDTO>builder()
                .result(draft)
                .build();
    }

    /**
     * Submit toàn bộ draft từ Redis → lưu chính thức vào DB
     */
    @PostMapping("/{report06Id}/submit")
    public ApiResponse<Report06DTO> submitDraft(
            @PathVariable UUID report06Id,
            @RequestParam UUID userAccountId) {

        Report06DTO result = report06Service.submitDraftToDatabase(report06Id, userAccountId);
        return ApiResponse.<Report06DTO>builder()
                .result(result)
                .message("Report06 has been successfully submitted and saved to database")
                .build();
    }
}