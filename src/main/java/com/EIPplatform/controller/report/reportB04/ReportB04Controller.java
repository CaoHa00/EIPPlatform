package com.EIPplatform.controller.report.reportB04;
import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.report.report05.*;
import com.EIPplatform.model.dto.report.reportB04.ReportB04DTO;
import com.EIPplatform.service.report.reportB04.ReportB04Service;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/v1/reports-b04")
@RequiredArgsConstructor
@Slf4j
public class ReportB04Controller {
    ReportB04Service reportB04Service;


    @GetMapping
    public ApiResponse<ReportB04DTO> createReport(
            @Valid @RequestParam UUID businessDetailId) {

        ReportB04DTO response = reportB04Service.getOrCreateReportByBusinessDetailId(businessDetailId);
        return ApiResponse.<ReportB04DTO>builder()
                .message("get template report B04 successfully")
                .result(response)
                .build();
    }
}
