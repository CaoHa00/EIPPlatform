package com.EIPplatform.service.report;

import com.EIPplatform.mapper.report.ReportA05Mapper;
import com.EIPplatform.mapper.report.WasteWaterDataMapper;
import com.EIPplatform.model.dto.report.report.CreateReportRequest;
import com.EIPplatform.model.dto.report.report.ReportA05DTO;
import com.EIPplatform.model.dto.report.report.ReportA05DraftDTO;
import com.EIPplatform.model.dto.report.report.WasteWaterDataDTO;
import com.EIPplatform.model.entity.report.ReportA05;
import com.EIPplatform.model.entity.report.WasteWaterData;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.repository.report.ReportA05Repository;
import com.EIPplatform.repository.user.BusinessDetailRepository;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportA05ServiceImpl implements ReportA05Service {
    
    ReportA05Repository reportA05Repository;
    BusinessDetailRepository businessDetailRepository;
    ReportCacheService reportCacheService;
    WasteWaterDataMapper wasteWaterDataMapper;

    @Override
    @Transactional
    public ReportA05DTO createReport(CreateReportRequest request) {
        log.info("Creating report for business: {}, year: {}", 
            request.getBusinessDetailId(), request.getReportYear());
        
        // // 1. Kiểm tra business có tồn tại
        // BusinessDetail businessDetail = businessDetailRepository
        //     .findById(request.getBusinessDetailId())
        //     .orElseThrow(() -> new RuntimeException(
        //         "Business not found: " + request.getBusinessDetailId()));
        
        // // 2. Generate report code đơn giản (UUID)
        // String reportCode = "RPT-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // // 3. Tạo report
        // ReportA05 report = ReportA05.builder()
        //     .reportCode(reportCode)
        //     .businessDetail(businessDetail)
        //     .reportYear(request.getReportYear())
        //     .reportingPeriod(request.getReportingPeriod())
        //     .version(1)
        //     .isDeleted(false)
        //     .completionPercentage(BigDecimal.ZERO)
        //     .build();
        
        // // 4. Lưu vào DB
        // ReportA05 saved = reportA05Repository.save(report);
        // log.info("Created report: {}", saved.getReportCode());
        
        // // 5. Trả về DTO
        // return ReportA05DTO.builder()
        //     .reportId(saved.getReportId())
        //     .reportCode(saved.getReportCode())
        //     .businessDetailId(businessDetail.getBusinessDetailId())
        //     .companyName(businessDetail.getCompanyName())
        //     .reportYear(saved.getReportYear())
        //     .reportingPeriod(saved.getReportingPeriod())
        //     .completionPercentage(saved.getCompletionPercentage())
        //     .createdAt(saved.getCreatedAt())
        //     .build();
        // 1. Kiểm tra business (CHỈ KHI CÓ businessDetailId)
    BusinessDetail businessDetail = null;
    if (request.getBusinessDetailId() != null) {
        businessDetail = businessDetailRepository
            .findById(request.getBusinessDetailId())
            .orElseThrow(() -> new RuntimeException(
                "Business not found: " + request.getBusinessDetailId()));
    }
    

    
    // 2. Generate report code đơn giản
    String reportCode = "RPT-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    
    // 3. Tạo report (businessDetail có thể null)
    ReportA05 report = ReportA05.builder()
        .reportCode(reportCode)
        .businessDetail(businessDetail) // CÓ THỂ NULL
        .reportYear(request.getReportYear())
        .reportingPeriod(request.getReportingPeriod())
        .version(1)
        .isDeleted(false)
        .completionPercentage(BigDecimal.ZERO)
        .build();
    
    // 4. Lưu vào DB
    ReportA05 saved = reportA05Repository.save(report);
    log.info("Created report: {}", saved.getReportCode());
    
    // 5. Trả về DTO
    return ReportA05DTO.builder()
        .reportId(saved.getReportId())
        .reportCode(saved.getReportCode())
        .businessDetailId(businessDetail != null ? businessDetail.getBusinessDetailId() : null)
        .companyName(businessDetail != null ? businessDetail.getCompanyName() : null)
        .reportYear(saved.getReportYear())
        .reportingPeriod(saved.getReportingPeriod())
        .completionPercentage(saved.getCompletionPercentage())
        .createdAt(saved.getCreatedAt())
        .build();
    }

    @Override
    public ReportA05DTO getReportById(UUID reportId) {
        ReportA05 report = reportA05Repository.findById(reportId)
            .orElseThrow(() -> new RuntimeException("Report not found: " + reportId));
        return ReportA05DTO.builder()
            .reportId(report.getReportId())
            .reportCode(report.getReportCode())
            .businessDetailId(report.getBusinessDetail() != null ? report.getBusinessDetail().getBusinessDetailId() : null)
            .companyName(report.getBusinessDetail() != null ? report.getBusinessDetail().getCompanyName() : null)
            .reportYear(report.getReportYear())
            .reportingPeriod(report.getReportingPeriod())
            .completionPercentage(report.getCompletionPercentage())
            .createdAt(report.getCreatedAt())
            .build();
    }

    @Override
    public WasteWaterDataDTO saveDraftWasteWaterData(UUID reportId, WasteWaterDataDTO data) {
        
        if(!reportA05Repository.existsById(reportId) ) {
            throw new RuntimeException("Report not found: " + reportId);
        }

              // Lưu vào cache qua ReportCacheService
        reportCacheService.updateWasteWaterData(reportId, data);
        log.info("Saved draft wastewater data to cache for report: {}", reportId);

        return data;
    }

    @Override
    public WasteWaterDataDTO getDraftWasteWaterData(UUID reportId) {
       log.info("Getting draft wastewater data from cache for report: {}", reportId);

       ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
         if (draft == null || draft.getWasteWaterData() == null) {
              log.warn("No draft wastewater data found in cache for report: {}", reportId);
              return null;
         }
        return draft.getWasteWaterData();
    }

    @Override
    public ReportA05DTO saveWasteWaterDataToDatabase(UUID reportId) {
       log.info("Saving wastewater data from cache to database for report: {}", reportId);
        
        // 1. Lấy draft data từ cache
        WasteWaterDataDTO draftData = getDraftWasteWaterData(reportId);
        if (draftData == null) {
            throw new RuntimeException("No draft wastewater data found in cache for report: " + reportId);
        }
        
        // 2. Lấy report
        ReportA05 report = reportA05Repository.findById(reportId)
            .orElseThrow(() -> new RuntimeException("Report not found: " + reportId));
        
        // 3. Nếu đã có wastewater data → update
        if (report.getWasteWaterData() != null) {
            log.info("Updating existing wastewater data");
            WasteWaterData existing = report.getWasteWaterData();
            wasteWaterDataMapper.updateEntityFromDTO(draftData, existing);
        } else {
            // 4. Nếu chưa có → tạo mới
            log.info("Creating new wastewater data");
            WasteWaterData wasteWaterData = wasteWaterDataMapper.toEntity(draftData);
            wasteWaterData.setReport(report);
            report.setWasteWaterData(wasteWaterData);
        }
        
        // 5. Cập nhật completion percentage
        // Integer completionPercentage = reportCacheService.calculateCompletionPercentage(reportId);
        // report.setCompletionPercentage(BigDecimal.valueOf(completionPercentage));
        
        // 6. Lưu vào DB
        ReportA05 saved = reportA05Repository.save(report);
        
        // 7. CẬP NHẬT cache (không xóa) - để user có thể edit tiếp
        // Nếu muốn xóa, gọi: reportCacheService.deleteDraftReport(reportId);
        
        log.info("Saved wastewater data to database");
        
        BusinessDetail bd = saved.getBusinessDetail();
        return ReportA05DTO.builder()
            .reportId(saved.getReportId())
            .reportCode(saved.getReportCode())
            .businessDetailId(bd != null ? bd.getBusinessDetailId() : null)
            .companyName(bd != null ? bd.getCompanyName() : null)
            .reportYear(saved.getReportYear())
            .reportingPeriod(saved.getReportingPeriod())
            .completionPercentage(saved.getCompletionPercentage())
            .createdAt(saved.getCreatedAt())
            .build();
    }

    @Override
    public void deleteDraftWasteWaterData(UUID reportId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteDraftWasteWaterData'");
    }

}
    