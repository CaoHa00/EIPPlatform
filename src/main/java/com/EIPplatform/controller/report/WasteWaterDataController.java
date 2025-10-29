package com.EIPplatform.controller.report;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EIPplatform.model.dto.report.report.WasteWaterDataDTO;
import com.EIPplatform.service.report.ReportCacheService;
import com.EIPplatform.service.report.WasteWaterDataService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/reports/{reportId}")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WasteWaterDataController {
    WasteWaterDataService wasteWaterDataService;
    ReportCacheService reportCacheService;


    @PutMapping("/wastewater/draft")
    public ResponseEntity<WasteWaterDataDTO> updateWasteWaterDraft(
        @PathVariable UUID reportId, 
        @Valid 
        @RequestBody WasteWaterDataDTO dto
        ){
        
        try {
            wasteWaterDataService.updateWasteWaterDataToCache(reportId, dto);
        } catch (Exception e) {
            
            e.printStackTrace();
        }

        return ResponseEntity.ok(dto);
    }
}
