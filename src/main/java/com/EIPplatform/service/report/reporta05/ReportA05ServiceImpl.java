package com.EIPplatform.service.report.reporta05;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.mapper.report.airemmissionmanagement.AirEmissionDataMapper;
import com.EIPplatform.mapper.report.wastemanagement.WasteManagementDataMapper;
import com.EIPplatform.mapper.report.wastewatermanager.WasteWaterDataMapper;
import com.EIPplatform.model.dto.report.report.*;
import com.EIPplatform.model.dto.report.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import com.EIPplatform.model.dto.report.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.dto.report.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;
import com.EIPplatform.model.entity.report.ReportA05;
import com.EIPplatform.model.entity.report.airemmissionmanagement.AirEmissionData;
import com.EIPplatform.model.entity.report.wastemanagement.WasteManagementData;
import com.EIPplatform.model.entity.report.wastewatermanager.WasteWaterData;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.repository.report.ReportA05Repository;
import com.EIPplatform.repository.report.airemmissionmanagement.AirEmissionDataRepository;
import com.EIPplatform.repository.report.wastemanagement.WasteManagementDataRepository;
import com.EIPplatform.repository.report.wastewatermanager.WasteWaterRepository;
import com.EIPplatform.repository.user.BusinessDetailRepository;
import com.EIPplatform.service.report.reportcache.ReportCacheService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated // Để enable method-level validation nếu cần
public class ReportA05ServiceImpl implements ReportA05Service {

        ReportA05Repository reportA05Repository;
        BusinessDetailRepository businessDetailRepository;
        ReportCacheService reportCacheService;
        WasteManagementDataMapper wasteManagementDataMapper;
        AirEmissionDataMapper airEmissionDataMapper;
        WasteWaterDataMapper wasteWaterDataMapper;
        ExceptionFactory exceptionFactory;
        WasteManagementDataRepository wasteManagementDataRepository;
        AirEmissionDataRepository airEmissionDataRepository;
        WasteWaterRepository wasteWaterDataRepository;

        @Override
        @Transactional
        public ReportA05DTO createReport(CreateReportRequest request) {
                // 1. Kiểm tra business (CHỈ KHI CÓ businessDetailId)
                BusinessDetail businessDetail = null;
                if (request.getBusinessDetailId() != null) {
                        businessDetail = businessDetailRepository
                                        .findById(request.getBusinessDetailId())
                                        .orElseThrow(() -> exceptionFactory.createNotFoundException("BusinessDetail",
                                                        request.getBusinessDetailId(), ReportError.BUSINESS_NOT_FOUND));
                }

                String reportCode = "RPT-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();

                ReportA05 report = ReportA05.builder()
                                .reportCode(reportCode)
                                .businessDetail(businessDetail)
                                .reportYear(request.getReportYear())
                                .reportingPeriod(request.getReportingPeriod())
                                .version(1)
                                .isDeleted(false)
                                .completionPercentage(BigDecimal.ZERO)
                                .build();

                ReportA05 saved = reportA05Repository.save(report);

                return ReportA05DTO.builder()
                                .reportId(saved.getReportId())
                                .reportCode(saved.getReportCode())
                                .businessDetailId(businessDetail != null ? businessDetail.getBusinessDetailId() : null)
                                .facilityName(businessDetail != null ? businessDetail.getFacilityName() : null)
                                .reportYear(saved.getReportYear())
                                .reportingPeriod(saved.getReportingPeriod())
                                .completionPercentage(saved.getCompletionPercentage())
                                .createdAt(saved.getCreatedAt())
                                .build();
        }

        @Override
        @Transactional
        public ReportA05DTO getReportById(UUID reportId) {

                // 1. Fetch basic report
                ReportA05 report = reportA05Repository.findByReportIdWithBasic(reportId)
                                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                                                "ReportA05", reportId, ReportError.REPORT_NOT_FOUND));

                // 2. Fetch WasteWaterData với collections
                WasteWaterDataDTO wasteWaterDataDTO = wasteWaterDataRepository
                                .findByReportIdWithCollections(reportId)
                                .map(wasteWaterDataMapper::toDto)
                                .orElse(null);

                // 3. Fetch WasteManagementData với collections
                WasteManagementDataDTO wasteManagementDataDTO = wasteManagementDataRepository
                                .findByReportIdWithCollections(reportId)
                                .map(wasteManagementDataMapper::toDto)
                                .orElse(null);

                // 4. Fetch AirEmissionData với collections
                AirEmissionDataDTO airEmissionDataDTO = airEmissionDataRepository
                                .findByReportIdWithCollections(reportId)
                                .map(airEmissionDataMapper::toDto)
                                .orElse(null);

                // 5. Build DTO
                return ReportA05DTO.builder()
                                .reportId(report.getReportId())
                                .reportCode(report.getReportCode())
                                .businessDetailId(report.getBusinessDetail() != null
                                                ? report.getBusinessDetail().getBusinessDetailId()
                                                : null)
                                .facilityName(report.getBusinessDetail() != null
                                                ? report.getBusinessDetail().getFacilityName()
                                                : null)
                                .reportYear(report.getReportYear())
                                .reportingPeriod(report.getReportingPeriod())
                                .wasteWaterData(wasteWaterDataDTO)
                                .wasteManagementData(wasteManagementDataDTO)
                                .airEmissionData(airEmissionDataDTO)
                                .reviewNotes(report.getReviewNotes())
                                .inspectionRemedyReport(report.getInspectionRemedyReport())
                                .completionPercentage(report.getCompletionPercentage())
                                .createdAt(report.getCreatedAt())
                                .build();
        }

        @Override
        public ReportA05DraftDTO getDraftData(UUID reportId) {
                ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
                if (draft == null) {
                        return null;
                }
                return draft;
        }

        /**
         * Cập nhật completion percentage cho draft dựa trên dữ liệu hiện tại
         * (Gọi sau mỗi step để tự động tính % và lưu lại cache)
         */
        @Override
        @Transactional
        public ReportA05DraftDTO updateDraftCompletion(UUID reportId) {
                ReportA05DraftDTO draft = getDraftData(reportId);
                if (draft == null) {
                        throw exceptionFactory.createCustomException(ReportError.DRAFT_NOT_FOUND);
                }
                int percentage = calculateCompletionPercentage(draft);
                draft.setCompletionPercentage(percentage);
                draft.setLastModified(LocalDateTime.now());
                reportCacheService.saveDraftReport(draft);
                log.info("Updated completion for report {}: {}%", reportId, percentage);
                return draft;
        }

        @Override
        @Transactional
        public ReportA05DTO submitDraftToDatabase(UUID reportId) {
                ReportA05DraftDTO draftData = getDraftData(reportId);
                if (draftData == null) {
                        throw exceptionFactory.createCustomException(ReportError.DRAFT_NOT_FOUND);
                }

                if (draftData.getCompletionPercentage() == null) {
                        int percentage = calculateCompletionPercentage(draftData);
                        draftData.setCompletionPercentage(percentage);
                }

                if (!isDraftComplete(draftData)) {
                        throw exceptionFactory.createValidationException("ReportA05Draft", "completionPercentage",
                                        (draftData.getCompletionPercentage() != null
                                                        ? draftData.getCompletionPercentage()
                                                        : 0),
                                        ReportError.DRAFT_INCOMPLETE);
                }

                ReportA05 report = reportA05Repository.findById(reportId)
                                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportA05", reportId,
                                                ReportError.REPORT_NOT_FOUND));

                saveOrUpdateWasteWaterData(report, draftData);
                saveOrUpdateWasteManagementData(report, draftData);
                saveOrUpdateAirEmissionData(report, draftData);

                if (draftData.getCompletionPercentage() != null) {
                        report.setCompletionPercentage(BigDecimal.valueOf(draftData.getCompletionPercentage()));
                }

                ReportA05 saved = reportA05Repository.save(report);

                draftData.setIsDraft(false);
                draftData.setLastModified(LocalDateTime.now());
                reportCacheService.deleteDraftReport(reportId);

                BusinessDetail bd = saved.getBusinessDetail();
                return ReportA05DTO.builder()
                                .reportCode(saved.getReportCode())
                                .businessDetailId(bd != null ? bd.getBusinessDetailId() : null)
                                .facilityName(bd != null ? bd.getFacilityName() : null)
                                .reportYear(saved.getReportYear())
                                .reportingPeriod(saved.getReportingPeriod())
                                .reviewNotes(saved.getReviewNotes())
                                .inspectionRemedyReport(saved.getInspectionRemedyReport())
                                .completionPercentage(saved.getCompletionPercentage())
                                .createdAt(saved.getCreatedAt())
                                .build();
        }

        @Override
        @Transactional
        public InspectionRemedyResponse updateInspectionRemedyReport(UUID reportId,
                        UpdateInspectionRemedyReportRequest request) {
                // Validate report tồn tại
                ReportA05 report = reportA05Repository.findById(reportId)
                                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportA05", reportId,
                                                ReportError.REPORT_NOT_FOUND));

                // Validate request không null và trường chính không null (nếu cần, nhưng @Size
                // đã handle length)
                if (request == null) {
                        throw exceptionFactory.createValidationException("UpdateInspectionRemedyReportRequest",
                                        "request", null,
                                        ReportError.INVALID_REQUEST);
                }
                if (Objects.isNull(request.getInspectionRemedyReport())) {
                        throw exceptionFactory.createValidationException("UpdateInspectionRemedyReportRequest",
                                        "inspectionRemedyReport", null, ReportError.FIELD_REQUIRED);
                }

                // Update trường
                report.setInspectionRemedyReport(request.getInspectionRemedyReport());
                report.setUpdatedAt(LocalDateTime.now()); // Cập nhật timestamp nếu cần

                ReportA05 saved = reportA05Repository.save(report);

                // Log
                log.info("Updated inspection remedy report for reportId: {}", reportId);

                // Build và return response mới đơn giản
                return InspectionRemedyResponse.builder()
                                .reportId(saved.getReportId())
                                .inspectionRemedyReport(saved.getInspectionRemedyReport())
                                .updatedAt(saved.getUpdatedAt())
                                .build();
        }

        private int calculateCompletionPercentage(ReportA05DraftDTO draft) {
                int score = 0;
                if (isSectionComplete(draft.getWasteWaterData()))
                        score += 33;
                if (isSectionComplete(draft.getWasteManagementData()))
                        score += 33;
                if (isSectionComplete(draft.getAirEmissionData()))
                        score += 34;
                return score;
        }

        private boolean isSectionComplete(Object sectionDto) {
                if (sectionDto == null)
                        return false;

                return true;
        }

        private boolean isDraftComplete(ReportA05DraftDTO draftData) {
                // Tính % nếu null
                if (draftData.getCompletionPercentage() == null) {
                        draftData.setCompletionPercentage(calculateCompletionPercentage(draftData));
                }
                boolean allFieldsFilled = draftData.getWasteWaterData() != null
                                && draftData.getWasteManagementData() != null
                                && draftData.getAirEmissionData() != null;
                boolean completionComplete = draftData.getCompletionPercentage() == 100;
                return allFieldsFilled && completionComplete;
        }

        private void saveOrUpdateWasteWaterData(ReportA05 report, ReportA05DraftDTO draftData) {
                WasteWaterDataDTO dto = draftData.getWasteWaterData(); // Response DTO từ draft
                if (dto == null)
                        return;

                WasteWaterData entity;
                if (report.getWasteWaterData() != null && report.getWasteWaterData().getWwId() != null) {
                        // Update: Merge partial từ DTO vào entity hiện có
                        entity = report.getWasteWaterData();
                        wasteWaterDataMapper.updateEntityFromDto(dto, entity); // Method cho WasteWaterDataDTO
                } else {
                        // Create: Chuyển từ response DTO sang entity mới
                        entity = wasteWaterDataMapper.dtoToEntity(dto); // Đúng method: DTO → entity
                        entity.setReport(report);
                        // @AfterMapping trong mapper sẽ handle null lists và parent references
                }
                report.setWasteWaterData(entity);
                // Sau đó save entity qua repo nếu cần
        }

        private void saveOrUpdateWasteManagementData(ReportA05 report, ReportA05DraftDTO draftData) {
                WasteManagementDataDTO dto = draftData.getWasteManagementData(); // Response DTO từ draft
                if (dto == null)
                        return;

                WasteManagementData entity;
                if (report.getWasteManagementData() != null && report.getWasteManagementData().getWmId() != null) {
                        // Update: Merge partial từ DTO vào entity hiện có
                        entity = report.getWasteManagementData();
                        wasteManagementDataMapper.updateEntityFromDto(dto, entity); // Method cho WasteManagementDataDTO
                } else {
                        // Create: Chuyển từ response DTO sang entity mới
                        entity = wasteManagementDataMapper.dtoToEntity(dto); // Đúng method: DTO → entity
                        entity.setReport(report);
                        // @AfterMapping trong mapper sẽ handle null lists và parent references
                }
                report.setWasteManagementData(entity);
                // Sau đó save entity qua repo nếu cần (ví dụ:
                // wasteManagementDataRepository.save(entity))
        }

        private void saveOrUpdateAirEmissionData(ReportA05 report, ReportA05DraftDTO draftData) {
                AirEmissionDataDTO dto = draftData.getAirEmissionData(); // Đây là response DTO từ draft
                if (dto == null)
                        return;

                AirEmissionData entity;
                if (report.getAirEmissionData() != null && report.getAirEmissionData().getAirEmissionDataId() != null) {
                        // Update: Merge partial từ DTO vào entity hiện có
                        entity = report.getAirEmissionData();
                        airEmissionDataMapper.updateEntityFromDto(dto, entity); // Method mới cho partial update từ DTO
                } else {
                        // Create: Chuyển từ response DTO sang entity mới (KHÔNG dùng toDto!)
                        entity = airEmissionDataMapper.dtoToEntity(dto); // ✅ Đúng method: DTO → entity
                        entity.setReport(report);
                        // @AfterMapping trong mapper sẽ handle null lists nếu cần
                }
                report.setAirEmissionData(entity);
                // Sau đó save entity qua repo nếu chưa (ví dụ:
                // airEmissionDataRepository.save(entity))
        }

        @Override
        public byte[] generateReportFile(UUID reportId) throws Exception {
                ReportA05 report = reportA05Repository.findById(reportId)
                                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportA05",
                                                reportId,
                                                ReportError.REPORT_NOT_FOUND));

                BusinessDetail business = report.getBusinessDetail();
                if (business == null) {
                        throw exceptionFactory.createCustomException(ReportError.BUSINESS_NOT_FOUND);
                }

                ReportA05DraftDTO draftData = getDraftData(reportId);
                WasteWaterDataDTO wasteWaterDataDTO = draftData != null ? draftData.getWasteWaterData() : null;

                // Map dữ liệu với key chính xác
                Map<String, String> data = new HashMap<>();
                data.put("facilityName", business.getFacilityName());
                data.put("address", business.getAddress());
                data.put("phoneNumber", business.getPhoneNumber());
                data.put("legalRepresentative", business.getLegalRepresentative());
                data.put("activityType", business.getActivityType());
                data.put("scaleCapacity", business.getScaleCapacity());
                data.put("ISO_certificate_14001",
                                business.getISO_certificate_14001() != null ? business.getISO_certificate_14001() : "");
                data.put("businessRegistrationNumber", business.getBusinessRegistrationNumber());
                data.put("taxCode", business.getTaxCode());
                data.put("operationType", business.getOperationType().name());
                data.put("seasonalDescription",
                                business.getSeasonalDescription() != null ? business.getSeasonalDescription() : "");

                if (wasteWaterDataDTO != null) {
                        data.put("ww_treatment_desc",
                                        wasteWaterDataDTO.getTreatmentWwDesc() != null
                                                        ? wasteWaterDataDTO.getTreatmentWwDesc()
                                                        : "");
                        // Nước thải sinh hoạt
                        data.put("domestic_ww_cy",
                                        wasteWaterDataDTO.getDomWwCy() != null
                                                        ? wasteWaterDataDTO.getDomWwCy().toString()
                                                        : "");
                        data.put("domestic_ww_py",
                                        wasteWaterDataDTO.getDomWwPy() != null
                                                        ? wasteWaterDataDTO.getDomWwPy().toString()
                                                        : "");
                        data.put("domestic_ww_design",
                                        wasteWaterDataDTO.getDomWwDesign() != null
                                                        ? wasteWaterDataDTO.getDomWwDesign().toString()
                                                        : "");
                        // Nước thải công nghiệp
                        data.put("industrial_ww_cy",
                                        wasteWaterDataDTO.getIndustrialWwCy() != null
                                                        ? wasteWaterDataDTO.getIndustrialWwCy().toString()
                                                        : "");
                        data.put("industrial_ww_py",
                                        wasteWaterDataDTO.getIndustrialWwPy() != null
                                                        ? wasteWaterDataDTO.getIndustrialWwPy().toString()
                                                        : "");
                        data.put("industrial_ww_design", wasteWaterDataDTO.getIndustrialWwDesign() != null
                                        ? wasteWaterDataDTO.getIndustrialWwDesign().toString()
                                        : "");
                        // Nước làm mát
                        data.put("cooling_water_cy",
                                        wasteWaterDataDTO.getCoolingWaterCy() != null
                                                        ? wasteWaterDataDTO.getCoolingWaterCy().toString()
                                                        : "");
                        data.put("cooling_water_py",
                                        wasteWaterDataDTO.getCoolingWaterPy() != null
                                                        ? wasteWaterDataDTO.getCoolingWaterPy().toString()
                                                        : "");
                        data.put("cooling_water_design",
                                        wasteWaterDataDTO.getCoolingWaterDesign() != null
                                                        ? wasteWaterDataDTO.getCoolingWaterDesign().toString()
                                                        : "");
                        // Tình hình đầu nối hệ thống XLNT tập trung
                        data.put("connection_status_desc",
                                        wasteWaterDataDTO.getConnectionStatusDesc() != null
                                                        ? wasteWaterDataDTO.getConnectionStatusDesc()
                                                        : "");
                        // kết quả quan trắc nước thải
                        // nước thải sinh hoạt
                        data.put("dom_monitor_period",
                                        wasteWaterDataDTO.getDomMonitorPeriod() != null
                                                        ? wasteWaterDataDTO.getDomMonitorPeriod()
                                                        : "");
                        data.put("dom_monitor_freq", wasteWaterDataDTO.getDomMonitorFreq() != null
                                        ? wasteWaterDataDTO.getDomMonitorFreq()
                                        : "");
                        data.put("dom_monitor_locations",
                                        wasteWaterDataDTO.getDomMonitorLocations() != null
                                                        ? wasteWaterDataDTO.getDomMonitorLocations()
                                                        : "");
                        data.put("dom_sample_count", wasteWaterDataDTO.getDomSampleCount() != null
                                        ? wasteWaterDataDTO.getDomSampleCount().toString()
                                        : "");
                        data.put("dom_qcvn_standard",
                                        wasteWaterDataDTO.getDomQcvnStandard() != null
                                                        ? wasteWaterDataDTO.getDomQcvnStandard()
                                                        : "");
                        data.put("dom_agency_name",
                                        wasteWaterDataDTO.getDomAgencyName() != null
                                                        ? wasteWaterDataDTO.getDomAgencyName()
                                                        : "");
                        data.put("dom_agency_vimcerts",
                                        wasteWaterDataDTO.getDomAgencyVimcerts() != null
                                                        ? wasteWaterDataDTO.getDomAgencyVimcerts()
                                                        : "");
                        // nước thải công nghiệp
                        data.put("ind_monitor_period",
                                        wasteWaterDataDTO.getIndMonitorPeriod() != null
                                                        ? wasteWaterDataDTO.getIndMonitorPeriod()
                                                        : "");
                        data.put("ind_monitor_freq",
                                        wasteWaterDataDTO.getIndMonitorFreq() != null
                                                        ? wasteWaterDataDTO.getIndMonitorFreq()
                                                        : "");
                        data.put("ind_monitor_locations",
                                        wasteWaterDataDTO.getIndMonitorLocations() != null
                                                        ? wasteWaterDataDTO.getIndMonitorLocations()
                                                        : "");
                        data.put("ind_sample_count",
                                        wasteWaterDataDTO.getIndSampleCount() != null
                                                        ? wasteWaterDataDTO.getIndSampleCount().toString()
                                                        : "");
                        data.put("ind_qcvn_standard",
                                        wasteWaterDataDTO.getIndQcvnStandard() != null
                                                        ? wasteWaterDataDTO.getIndQcvnStandard()
                                                        : "");
                        data.put("ind_agency_name",
                                        wasteWaterDataDTO.getIndAgencyName() != null
                                                        ? wasteWaterDataDTO.getIndAgencyName()
                                                        : "");
                        data.put("ind_agency_vimcerts",
                                        wasteWaterDataDTO.getIndAgencyVimcerts() != null
                                                        ? wasteWaterDataDTO.getIndAgencyVimcerts()
                                                        : "");
                        // Quan trắc nước thải liên tục tự động (Nếu có
                        // thông tin chung
                        data.put("auto_station_location",
                                        wasteWaterDataDTO.getAutoStationLocation() != null
                                                        ? wasteWaterDataDTO.getAutoStationLocation()
                                                        : "");
                        data.put("auto_station_GPS",
                                        wasteWaterDataDTO.getAutoStationGps() != null
                                                        ? wasteWaterDataDTO.getAutoStationGps()
                                                        : "");
                        data.put("auto_station_map",
                                        wasteWaterDataDTO.getAutoStationMap() != null
                                                        ? wasteWaterDataDTO.getAutoStationMap()
                                                        : "");
                        data.put("auto_source_desc",
                                        wasteWaterDataDTO.getAutoSourceDesc() != null
                                                        ? wasteWaterDataDTO.getAutoSourceDesc()
                                                        : "");
                        data.put("auto_data_frequency",
                                        wasteWaterDataDTO.getAutoDataFrequency() != null
                                                        ? wasteWaterDataDTO.getAutoDataFrequency()
                                                        : "");
                        data.put("auto_calibration_info",
                                        wasteWaterDataDTO.getAutoCalibrationInfo() != null
                                                        ? wasteWaterDataDTO.getAutoCalibrationInfo()
                                                        : "");
                        // tình trangh haot động của trạm
                        data.put("auto_incident_summary",
                                        wasteWaterDataDTO.getAutoIncidentSummary() != null
                                                        ? wasteWaterDataDTO.getAutoIncidentSummary()
                                                        : "");
                        data.put("auto_downtime_desc",
                                        wasteWaterDataDTO.getAutoDowntimeDesc() != null
                                                        ? wasteWaterDataDTO.getAutoDowntimeDesc()
                                                        : "");
                        // nhận xét kết quả quan trắc
                        data.put("auto_exceed_days_summary",
                                        wasteWaterDataDTO.getAutoExceedDaysSummary() != null
                                                        ? wasteWaterDataDTO.getAutoExceedDaysSummary()
                                                        : "");
                        data.put("auto_abnormal_reason",
                                        wasteWaterDataDTO.getAutoAbnormalReason() != null
                                                        ? wasteWaterDataDTO.getAutoAbnormalReason()
                                                        : "");
                        // kết luận
                        data.put("auto_completeness_review",
                                        wasteWaterDataDTO.getAutoCompletenessReview() != null
                                                        ? wasteWaterDataDTO.getAutoCompletenessReview()
                                                        : "");
                        data.put("auto_exceed_summary",
                                        wasteWaterDataDTO.getAutoExceedSummary() != null
                                                        ? wasteWaterDataDTO.getAutoExceedSummary()
                                                        : "");
                }

                Resource resource = new ClassPathResource("templates/reportA05/ReportA05_template.docx");
                log.info("Loading template from: {}", resource.getFilename());

                try (InputStream fis = resource.getInputStream();
                                XWPFDocument doc = new XWPFDocument(fis);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                        // Thay thế trong paragraphs
                        for (XWPFParagraph paragraph : doc.getParagraphs()) {
                                replacePlaceholders(paragraph, data);
                        }

                        // Thay thế trong tables
                        for (XWPFTable table : doc.getTables()) {
                                for (XWPFTableRow row : table.getRows()) {
                                        for (XWPFTableCell cell : row.getTableCells()) {
                                                for (XWPFParagraph p : cell.getParagraphs()) {
                                                        replacePlaceholders(p, data);
                                                }
                                        }
                                }
                        }
                        if (wasteWaterDataDTO != null) {
                                // Bảng 1.1, 1.2 Thống Kê Vị Trí & Kết Quả Vượt Quy Chuẩn (QCVN) (nếu có)
                                if (wasteWaterDataDTO.getMonitoringExceedances() != null
                                                && !wasteWaterDataDTO.getMonitoringExceedances().isEmpty()) {
                                        log.info(" Filling Monitoring Exceedances table ({} records)",
                                                        wasteWaterDataDTO.getMonitoringExceedances().size());
                                        TableMappingService.fillWasteWaterMonitoringTable(doc,
                                                        wasteWaterDataDTO.getMonitoringExceedances());
                                } else {
                                        log.info("No Monitoring Exceedances data to fill.");
                                }
                                // bảng 1.3 Thống kê kết quả quan trắc tự động
                                if (wasteWaterDataDTO.getMonitoringStats() != null
                                                && !wasteWaterDataDTO.getMonitoringStats().isEmpty()) {
                                        log.info(" Filling Monitoring Stats table ({} records)",
                                                        wasteWaterDataDTO.getMonitoringStats().size());
                                        TableMappingService.fillAutoMonitoringStatsTable(doc,
                                                        wasteWaterDataDTO.getMonitoringStats());
                                } else {
                                        log.info("No Monitoring Stats data to fill.");
                                }
                                // bảng 1.4 Thống kê các sự cố của trạm
                                if (wasteWaterDataDTO.getMonitoringIncidents() != null
                                                && !wasteWaterDataDTO.getMonitoringIncidents().isEmpty()) {
                                        log.info(" Filling Monitoring Incidents table ({} records)",
                                                        wasteWaterDataDTO.getMonitoringIncidents().size());
                                        TableMappingService.fillAutoMonitoringIncidentsTable(doc,
                                                        wasteWaterDataDTO.getMonitoringIncidents());
                                } else {
                                        log.info("No Monitoring Incidents data to fill.");
                                }
                                // Bảng 1.5: Thống kê vượt QCVN (theo từng thông số)
                                if (wasteWaterDataDTO.getQcvnExceedances() != null
                                                && !wasteWaterDataDTO.getQcvnExceedances().isEmpty()) {
                                        log.info(" Filling QCVN Exceedances table ({} records)",
                                                        wasteWaterDataDTO.getQcvnExceedances().size());
                                        TableMappingService.fillQcvnExceedancesTable(doc,
                                                        wasteWaterDataDTO.getQcvnExceedances());
                                } else {
                                        log.info("No QCVN Exceedances data to fill.");
                                }
                        }
                        doc.write(baos);
                        byte[] result = baos.toByteArray();

                        // Ghi ra file để kiểm tra (optional)
                        String outputDir = "D:\\Cao Ha\\eipFolder\\generated\\reports";
                        Files.createDirectories(Paths.get(outputDir));

                        String fileName = String.format("%s/ReportA05_%s_%s.docx",
                                        outputDir,
                                        business.getFacilityName().replaceAll("[^a-zA-Z0-9]", "_"),
                                        reportId);

                        Files.write(Paths.get(fileName), result);
                        log.info(" File generated: {}", fileName);

                        return result;
                }
        }

        /**
         * PHƯƠNG THỨC ĐÃ SỬA - Xử lý placeholder bị tách thành nhiều runs
         */
        private void replacePlaceholders(XWPFParagraph paragraph, Map<String, String> data) {
                // Lấy toàn bộ text của paragraph
                String fullText = paragraph.getText();
                if (fullText == null || fullText.isEmpty()) {
                        return;
                }

                // Thay thế tất cả placeholders
                boolean modified = false;
                for (Map.Entry<String, String> entry : data.entrySet()) {
                        String placeholder = "{{" + entry.getKey() + "}}";
                        if (fullText.contains(placeholder)) {
                                String value = entry.getValue();
                                if (value == null) {
                                        value = ""; // Thay thế null bằng empty string
                                }
                                fullText = fullText.replace(placeholder, value);
                                modified = true;
                        }
                }

                // Nếu có thay đổi, xóa hết runs cũ và tạo run mới
                if (modified) {
                        // Lưu formatting của run đầu tiên (nếu có)
                        XWPFRun firstRun = paragraph.getRuns().isEmpty() ? null : paragraph.getRuns().get(0);

                        // Xóa tất cả runs cũ
                        int runCount = paragraph.getRuns().size();
                        for (int i = runCount - 1; i >= 0; i--) {
                                paragraph.removeRun(i);
                        }

                        // Tạo run mới với text đã thay thế
                        XWPFRun newRun = paragraph.createRun();
                        newRun.setText(fullText);

                        // Copy formatting từ run cũ nếu có
                        if (firstRun != null) {
                                copyRunFormatting(firstRun, newRun);
                        }
                }
        }

        /**
         * PHƯƠNG THỨC MỚI - Copy formatting từ run cũ sang run mới
         */
        private void copyRunFormatting(XWPFRun source, XWPFRun target) {
                try {
                        if (source.getFontFamily() != null) {
                                target.setFontFamily(source.getFontFamily());
                        }
                        if (source.getFontSize() != -1) {
                                target.setFontSize(source.getFontSize());
                        }
                        target.setBold(source.isBold());
                        target.setItalic(source.isItalic());
                        target.setUnderline(source.getUnderline());
                        if (source.getColor() != null) {
                                target.setColor(source.getColor());
                        }
                } catch (Exception e) {
                        log.warn("Could not copy all formatting: {}", e.getMessage());
                }
        }
}
