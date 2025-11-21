package com.EIPplatform.service.report.reporta05;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.mapper.report.report05.airemmissionmanagement.AirEmissionDataMapper;
import com.EIPplatform.mapper.report.report05.wastemanagement.WasteManagementDataMapper;
import com.EIPplatform.mapper.report.report05.wastewatermanager.WasteWaterDataMapper;
import com.EIPplatform.model.dto.report.report05.*;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;

import com.EIPplatform.model.entity.permitshistory.EnvPermits;
import com.EIPplatform.model.entity.report.report05.ReportA05;
import com.EIPplatform.model.entity.report.report05.airemmissionmanagement.AirEmissionData;
import com.EIPplatform.model.entity.report.report05.wastemanagement.WasteManagementData;
import com.EIPplatform.model.entity.report.report05.wastewatermanager.WasteWaterData;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.user.businessInformation.BusinessHistoryConsumption;
import com.EIPplatform.repository.report.ReportA05Repository;
import com.EIPplatform.repository.report.report05.airemmissionmanagement.AirEmissionDataRepository;
import com.EIPplatform.repository.report.report05.wastemanagement.WasteManagementDataRepository;
import com.EIPplatform.repository.report.report05.wastewatermanager.WasteWaterRepository;
import com.EIPplatform.repository.user.BusinessDetailRepository;
import com.EIPplatform.service.report.reportcache.ReportCacheService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Double;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Value;
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
        @NonFinal
        @Value("${app.storage.local.upload-dir:/app/uploads}")
        private String uploadDir;

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
                .completionPercentage(0.0)
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
    public ReportA05DraftDTO getDraftData(UUID reportId, UUID userAccountId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
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
    public ReportA05DraftDTO updateDraftCompletion(UUID reportId, UUID userAccountId) {
        ReportA05DraftDTO draft = getDraftData(reportId, userAccountId);
        if (draft == null) {
            throw exceptionFactory.createCustomException(ReportError.DRAFT_NOT_FOUND);
        }
        int percentage = calculateCompletionPercentage(draft);
        draft.setCompletionPercentage(percentage);
        draft.setLastModified(LocalDateTime.now());
        reportCacheService.saveDraftReport(draft, userAccountId);
        log.info("Updated completion for report {} (user {}): {}%", reportId, userAccountId, percentage);
        return draft;
    }

    @Override
    @Transactional
    public ReportA05DTO submitDraftToDatabase(UUID reportId, UUID userAccountId) {
        ReportA05DraftDTO draftData = getDraftData(reportId, userAccountId);
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
            report.setCompletionPercentage(Double.valueOf(draftData.getCompletionPercentage()));
        }

        ReportA05 saved = reportA05Repository.save(report);

        reportCacheService.deleteDraftReport(reportId, userAccountId);

        WasteManagementDataDTO wasteManagementDataDTO = null;
        if (saved.getWasteManagementData() != null) {
            wasteManagementDataDTO = wasteManagementDataMapper.toDto(saved.getWasteManagementData()); // Gọi
            // trực
            // tiếp!
        }

        AirEmissionDataDTO airEmissionDataDTO = null;
        if (saved.getAirEmissionData() != null) {
            airEmissionDataDTO = airEmissionDataMapper.toDto(saved.getAirEmissionData());
        }

        WasteWaterDataDTO wasteWaterDataDTO = null;
        if (saved.getWasteWaterData() != null) {
            wasteWaterDataDTO = wasteWaterDataMapper.toDto(saved.getWasteWaterData());
        }

        BusinessDetail bd = saved.getBusinessDetail();
        return ReportA05DTO.builder()
                .reportId(saved.getReportId())
                .reportCode(saved.getReportCode())
                .businessDetailId(bd != null ? bd.getBusinessDetailId() : null)
                .facilityName(bd != null ? bd.getFacilityName() : null)
                .reportYear(saved.getReportYear())
                .reportingPeriod(saved.getReportingPeriod())
                .reviewNotes(saved.getReviewNotes())
                .airEmissionData(airEmissionDataDTO)
                .wasteManagementData(wasteManagementDataDTO)
                .wasteWaterData(wasteWaterDataDTO)
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
        if (draft.getWasteWaterData() != null
                && draft.getWasteManagementData() != null
                && draft.getAirEmissionData() != null) {
            return 100;
        }
        return 0;
    }

    private boolean isSectionComplete(Object sectionDto) {
        if (sectionDto == null)
            return false;

        return true;
    }

    private boolean isDraftComplete(ReportA05DraftDTO draftData) {
        return draftData.getWasteWaterData() != null
                && draftData.getWasteManagementData() != null
                && draftData.getAirEmissionData() != null;
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

            entity = airEmissionDataMapper.dtoToEntity(dto); // Đúng method: DTO → entity

            entity.setReport(report);
            // @AfterMapping trong mapper sẽ handle null lists nếu cần
        }
        report.setAirEmissionData(entity);
        // Sau đó save entity qua repo nếu chưa (ví dụ:
        // airEmissionDataRepository.save(entity))
    }

    @Override
    public byte[] generateReportFile(UUID reportId, UUID userAccountId) throws Exception {
        ReportA05 report = reportA05Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportA05",
                        reportId,
                        ReportError.REPORT_NOT_FOUND));

        BusinessDetail business = report.getBusinessDetail();
        if (business == null) {
            throw exceptionFactory.createCustomException(ReportError.BUSINESS_NOT_FOUND);
        }

        ReportA05DraftDTO draftData = getDraftData(reportId, userAccountId);
        WasteWaterDataDTO wasteWaterDataDTO = draftData != null ? draftData.getWasteWaterData() : null;
        AirEmissionDataDTO airEmissionDataDTO = draftData != null ? draftData.getAirEmissionData() : null;
        WasteManagementDataDTO wasteManagementDataDTO = draftData != null ? draftData.getWasteManagementData()
                : null;
        EnvPermits envPermits = business.getEnvPermits();
        List<BusinessHistoryConsumption> businessHistoryConsumptions = business
                .getBusinessHistoryConsumptions();
        LocalDate today = LocalDate.now();
        String day = String.valueOf(today.getDayOfMonth());
        String month = String.valueOf(today.getMonthValue());
        String year = String.valueOf(today.getYear());
        // FIX: Map dữ liệu với key chính xác
        Map<String, String> data = new HashMap<>();
        data.put("facility_name", business.getFacilityName());
        data.put("address", business.getAddress());
        data.put("phone_number", business.getPhoneNumber());
        data.put("legal_representative", business.getLegalRepresentative());
        data.put("activity_type", business.getActivityType());
        data.put("scale_capacity", business.getScaleCapacity());
        data.put("iso_14001_certificate",
                business.getISO_certificate_14001() != null ? business.getISO_certificate_14001() : "");
        data.put("business_license_number", business.getBusinessRegistrationNumber());
        data.put("tax_code", business.getTaxCode());
        data.put("seasonal_period", business.getOperationType().name());

        // permit
        if (envPermits == null) {
            log.warn("EnvPermits is null, skipping permit mapping.");
        } else {
            data.put("env_permit_number", envPermits.getPermitNumber());
            data.put("env_permit_issue_date", formatDate(envPermits.getIssueDate()));
            data.put("env_permit_issuer", envPermits.getIssuerOrg());
            data.put("env_permit_file", envPermits.getPermitFilePath());
        }

        // business history
        for (BusinessHistoryConsumption bhc : businessHistoryConsumptions) {
            data.put("product_volume_cy",
                    bhc.getProductVolumeCy() != null ? bhc.getProductVolumeCy().toString() : "");
            data.put("product_unit_cy", bhc.getProductUnitCy());
            data.put("product_volume_py",
                    bhc.getProductVolumePy() != null ? bhc.getProductVolumePy().toString() : "");
            data.put("product_unit_py", bhc.getProductUnitPy());
            data.put("fuel_consumption_cy",
                    bhc.getFuelConsumptionCy() != null ? bhc.getFuelConsumptionCy().toString()
                            : "");
            data.put("fuel_unit_cy", bhc.getFuelUnitCy());
            data.put("fuel_consumption_py",
                    bhc.getFuelConsumptionPy() != null ? bhc.getFuelConsumptionPy().toString()
                            : "");
            data.put("fuel_unit_py", bhc.getFuelUnitPy());
            data.put("electricity_consumption_cy",
                    bhc.getElectricityConsumptionCy() != null
                            ? bhc.getElectricityConsumptionCy().toString()
                            : "");
            data.put("electricity_consumption_py",
                    bhc.getElectricityConsumptionPy() != null
                            ? bhc.getElectricityConsumptionPy().toString()
                            : "");
            data.put("water_consumption_cy",
                    bhc.getWaterConsumptionCy() != null ? bhc.getWaterConsumptionCy().toString()
                            : "");
            data.put("water_consumption_py",
                    bhc.getWaterConsumptionPy() != null ? bhc.getWaterConsumptionPy().toString()
                            : "");
        }
        data.put("dateStr", day);
        data.put("monthYearStr", month);
        data.put("yearStr", year);

        if (wasteWaterDataDTO != null) {
            log.debug("Log wasteWaterDATAdto");
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
        if (airEmissionDataDTO != null) {
            data.put("air_treatment_desc",
                    airEmissionDataDTO.getAirTreatmentDesc() != null
                            ? airEmissionDataDTO.getAirTreatmentDesc()
                            : "");
            data.put("air_emission_cy",
                    airEmissionDataDTO.getAirEmissionCy() != null
                            ? airEmissionDataDTO.getAirEmissionCy().toString()
                            : "");
            data.put("air_emission_py",
                    airEmissionDataDTO.getAirEmissionPy() != null
                            ? airEmissionDataDTO.getAirEmissionPy().toString()
                            : "");
            data.put("air_monitor_period",
                    airEmissionDataDTO.getAirMonitorPeriod() != null
                            ? airEmissionDataDTO.getAirMonitorPeriod()
                            : "");
            data.put("air_monitor_freq", airEmissionDataDTO.getAirMonitorFreq() != null
                    ? airEmissionDataDTO.getAirMonitorFreq()
                    : "");
            data.put("air_monitor_locations",
                    airEmissionDataDTO.getAirMonitorLocations() != null
                            ? airEmissionDataDTO.getAirMonitorLocations()
                            : "");
            data.put("air_sample_count", airEmissionDataDTO.getAirSampleCount() != null
                    ? airEmissionDataDTO.getAirSampleCount().toString()
                    : "");
            data.put("air_qcvn_standard",
                    airEmissionDataDTO.getAirQcvnStandard() != null
                            ? airEmissionDataDTO.getAirQcvnStandard()
                            : "");
            data.put("air_agency_name",
                    airEmissionDataDTO.getAirAgencyName() != null
                            ? airEmissionDataDTO.getAirAgencyName()
                            : "");
            data.put("air_agency_vimcerts",
                    airEmissionDataDTO.getAirAgencyVimcerts() != null
                            ? airEmissionDataDTO.getAirAgencyVimcerts()
                            : "");
            data.put("air_auto_station_location",
                    airEmissionDataDTO.getAirAutoStationLocation() != null
                            ? airEmissionDataDTO.getAirAutoStationLocation()
                            : "");
            data.put("air_auto_station_GPS",
                    airEmissionDataDTO.getAirAutoStationGps() != null
                            ? airEmissionDataDTO.getAirAutoStationGps()
                            : "");
            data.put("air_auto_station_map",
                    airEmissionDataDTO.getAirAutoStationMapFilePath() != null
                            ? airEmissionDataDTO.getAirAutoStationMapFilePath()
                            : "");
            data.put("air_auto_source_desc",
                    airEmissionDataDTO.getAirAutoSourceDesc() != null
                            ? airEmissionDataDTO.getAirAutoSourceDesc()
                            : "");
            data.put("air_auto_data_frequency",
                    airEmissionDataDTO.getAirAutoDataFrequency() != null
                            ? airEmissionDataDTO.getAirAutoDataFrequency()
                            : "");
            data.put("air_auto_param_list",
                    airEmissionDataDTO.getAirAutoParamList() != null
                            ? airEmissionDataDTO.getAirAutoParamList()
                            : "");
            data.put("air_auto_calibration_info",
                    airEmissionDataDTO.getAirAutoCalibrationInfo() != null
                            ? airEmissionDataDTO.getAirAutoCalibrationInfo()
                            : "");
            data.put("air_auto_incident_summary",
                    airEmissionDataDTO.getAirAutoIncidentSummary() != null
                            ? airEmissionDataDTO.getAirAutoIncidentSummary()
                            : "");
            data.put("air_auto_downtime_desc",
                    airEmissionDataDTO.getAirAutoDowntimeDesc() != null
                            ? airEmissionDataDTO.getAirAutoDowntimeDesc()
                            : "");
            data.put("air_auto_avg_calc_desc",
                    airEmissionDataDTO.getAirAutoAvgCalcDesc() != null
                            ? airEmissionDataDTO.getAirAutoAvgCalcDesc()
                            : "");
            data.put("air_auto_avg_compare_desc",
                    airEmissionDataDTO.getAirAutoAvgCompareDesc() != null
                            ? airEmissionDataDTO.getAirAutoAvgCompareDesc()
                            : "");
            data.put("air_auto_exceed_days_summary",
                    airEmissionDataDTO.getAirAutoExceedDaysSummary() != null
                            ? airEmissionDataDTO.getAirAutoExceedDaysSummary()
                            : "");
            data.put("air_auto_abnormal_reason",
                    airEmissionDataDTO.getAirAutoAbnormalReason() != null
                            ? airEmissionDataDTO.getAirAutoAbnormalReason()
                            : "");
            data.put("air_auto_completeness_review",
                    airEmissionDataDTO.getAirAutoCompletenessReview() != null
                            ? airEmissionDataDTO.getAirAutoCompletenessReview()
                            : "");
            data.put("air_auto_exceed_conclusion",
                    airEmissionDataDTO.getAirAutoExceedConclusion() != null
                            ? airEmissionDataDTO.getAirAutoExceedConclusion()
                            : "");
        }
        if (wasteManagementDataDTO != null) {
            data.put("sw_general_notes",
                    wasteManagementDataDTO.getSwGeneralNote() != null
                            ? wasteManagementDataDTO.getSwGeneralNote()
                            : "");
            data.put("incident_plan_development",
                    wasteManagementDataDTO.getIncidentPlanDevelopment() != null
                            ? wasteManagementDataDTO.getIncidentPlanDevelopment()
                            : "");
            data.put("incident_prevention_measures",
                    wasteManagementDataDTO.getIncidentPreventionMeasures() != null
                            ? wasteManagementDataDTO.getIncidentPreventionMeasures()
                            : "");
            data.put("incident_response_report",
                    wasteManagementDataDTO.getIncidentResponseReport() != null
                            ? wasteManagementDataDTO.getIncidentResponseReport()
                            : "");

        }
        Resource resource = new ClassPathResource("templates/reportA05/ReportA05_template_v2.docx");
        log.info("Loading template from: {}", resource.getFilename());

        try (InputStream fis = resource.getInputStream();
             XWPFDocument doc = new XWPFDocument(fis);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            /* ============================================================
             * 1) Replace text placeholders everywhere
             * ============================================================ */
            doc.getParagraphs().forEach(p -> replacePlaceholders(p, data, false));

            for (XWPFTable table : doc.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        // Paragraphs inside a table → table mode
                        cell.getParagraphs().forEach(p -> replacePlaceholders(p, data, true));
                    }
                }
            }

            /* ============================================================
             * 2) SECTION 1 – WASTE WATER
             * ============================================================ */
            if (wasteWaterDataDTO != null) {

                fillOrRemove(
                        "Monitoring Exceedances",
                        wasteWaterDataDTO.getMonitoringExceedances(),
                        doc,
                        "Bảng 1.1",
                        list -> TableMappingService.fillWasteWaterMonitoringTable(doc, list)
                );

                fillOrRemove(
                        "Monitoring Stats",
                        wasteWaterDataDTO.getMonitoringStats(),
                        doc,
                        "Bảng 1.2",
                        list -> TableMappingService.fillAutoMonitoringStatsTable(doc, list)
                );

                fillOrRemove(
                        "Monitoring Incidents",
                        wasteWaterDataDTO.getMonitoringIncidents(),
                        doc,
                        "Bảng 1.3",
                        list -> TableMappingService.fillAutoMonitoringIncidentsTable(doc, list)
                );

                fillOrRemove(
                        "QCVN Exceedances",
                        wasteWaterDataDTO.getQcvnExceedances(),
                        doc,
                        "Bảng 1.4",
                        list -> TableMappingService.fillQcvnExceedancesTable(doc, list)
                );
            }


            /* ============================================================
             * 3) SECTION 2 – AIR EMISSION
             * ============================================================ */
            if (airEmissionDataDTO != null) {

                fillOrRemove(
                        "Air Monitoring Exceedances",
                        airEmissionDataDTO.getAirMonitoringExceedances(),
                        doc,
                        "Bảng 2.1",
                        list -> TableMappingService.fillAirMonitoringTable(doc, list)
                );

                fillOrRemove(
                        "Air Auto Monitoring Stats",
                        airEmissionDataDTO.getAirAutoMonitoringStats(),
                        doc,
                        "Bảng 2.2",
                        list -> TableMappingService.fillAirAutoMonitoringTable(doc, list)
                );

                fillOrRemove(
                        "Air Auto Incidents",
                        airEmissionDataDTO.getAirAutoMonitoringIncidents(),
                        doc,
                        "Bảng 2.3",
                        list -> TableMappingService.fillAirAutoMonitoringIncidentsTable(doc, list)
                );

                fillOrRemove(
                        "Air QCVN Exceedances",
                        airEmissionDataDTO.getAirAutoQcvnExceedances(),
                        doc,
                        "Bảng 2.4",
                        list -> TableMappingService.fillAirQcvnExceedancesTable(doc, list)
                );
            }


            /* ============================================================
             * 4) SECTION 3–4 – SOLID WASTE MANAGEMENT
             * ============================================================ */
            if (wasteManagementDataDTO != null) {

                fillOrRemove(
                        "Domestic SW",
                        wasteManagementDataDTO.getDomesticSolidWasteStats(),
                        doc,
                        "Bảng 3.1",
                        list -> TableMappingService.fillDomesticSolidWasteStatsTable(doc, list)
                );

                fillOrRemove(
                        "Industrial SW",
                        wasteManagementDataDTO.getIndustrialSolidWasteStats(),
                        doc,
                        "Bảng 3.2",
                        list -> TableMappingService.fillIndustrialSolidWasteStatsTable(doc, list)
                );

                fillOrRemove(
                        "Recycle SW",
                        wasteManagementDataDTO.getRecycleIndustrialWasteStats(),
                        doc,
                        "Bảng 3.3",
                        list -> TableMappingService.fillRecycleIndustrialWasteTable(doc, list)
                );

                fillOrRemove(
                        "Other SW",
                        wasteManagementDataDTO.getOtherSolidWasteStats(),
                        doc,
                        "Bảng 3.4",
                        list -> TableMappingService.fillOtherSolidWasteStatsTable(doc, list)
                );

                fillOrRemove(
                        "Hazardous SW",
                        wasteManagementDataDTO.getHazardousWasteStats(),
                        doc,
                        "Bảng 4.1",
                        list -> TableMappingService.fillHazardousWasteStatsTable(doc, list)
                );

                fillOrRemove(
                        "Exported HW",
                        wasteManagementDataDTO.getExportedHwStats(),
                        doc,
                        "Bảng 4.2",
                        list -> TableMappingService.fillExportedHwStatsTable(doc, list)
                );

                fillOrRemove(
                        "Self-Treated HW",
                        wasteManagementDataDTO.getSelfTreatedHwStats(),
                        doc,
                        "Bảng 4.3",
                        list -> TableMappingService.fillSelfTreatedHwStatsTable(doc, list)
                );

                fillOrRemove(
                        "POP Inventory",
                        wasteManagementDataDTO.getPopInventoryStats(),
                        doc,
                        "Bảng 4.4",
                        list -> TableMappingService.fillPopInventoryStatsTable(doc, list)
                );
            }


            /* ============================================================
             * 5) WRITE RESULT + SAVE FILE
             * ============================================================ */
            doc.write(baos);
            byte[] result = baos.toByteArray();

            ReportA05DTO reportDTO = ReportA05DTO.builder()
                    .reportYear(report.getReportYear())
                    .build();

            String savedFilePath = saveReportFile(result, reportId, business, reportDTO);
            log.info("Report file generated and saved: {} ({} bytes)", savedFilePath, result.length);

            return result;

        }

    }


    private String saveReportFile(byte[] fileBytes, UUID reportId, BusinessDetail business, ReportA05DTO report) {
        try {
            // Tạo subfolder theo năm: reporta05/2025/
            Integer reportYear = report.getReportYear() != null ? report.getReportYear()
                    : LocalDateTime.now().getYear();
            Path reportDir = Paths.get(uploadDir, "reporta05", String.valueOf(reportYear));

            // Tạo folder nếu chưa có
            Files.createDirectories(reportDir);
            log.info("📁 Report directory: {}", reportDir);

            // Tạo tên file
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String facilityName = business.getFacilityName() != null
                    ? sanitizeFileName(business.getFacilityName())
                    : "Unknown";

            String fileName = String.format("BaoCaoA05_%s_%s_%s.docx",
                    facilityName,
                    reportId.toString().substring(0, 8),
                    timestamp);

            // Lưu file
            Path filePath = reportDir.resolve(fileName);
            Files.write(filePath, fileBytes);

            // Return relative path
            String relativePath = String.format("reporta05/%d/%s", reportYear, fileName);
            log.info(" File saved successfully: {}", relativePath);

            return relativePath;

        } catch (IOException e) {
            log.error("Could not save report file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save report file", e);
        }
    }

    private String sanitizeFileName(String input) {
        if (input == null || input.isEmpty()) {
            return "Unknown";
        }

        String sanitized = input
                .replaceAll("[/\\\\:*?\"<>|]", "") // Loại bỏ ký tự không hợp lệ
                .replaceAll("\\s+", "_") // Thay space = underscore
                .trim();

        // Giới hạn độ dài
        if (sanitized.length() > 50) {
            sanitized = sanitized.substring(0, 50);
        }

        return sanitized;
    }

    /**
     * Format LocalDate to dd/MM/yyyy or return empty if null
     */
    private String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    private void replacePlaceholders(
            XWPFParagraph paragraph,
            Map<String, String> data,
            boolean inTable
    ) {
        String fullText = paragraph.getText();
        if (fullText == null || fullText.isEmpty()) return;
        if (!fullText.contains("{{")) return;

        boolean modified = false;

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String placeholder = "{{" + key + "}}";

            if (fullText.contains(placeholder)) {
                String value = entry.getValue();

                if (value == null || value.isEmpty()) {
                    value = inTable ? "" : ".........";
                    log.warn("Missing value for key '{}', using fallback '{}'", key, value);
                }

                fullText = fullText.replace(placeholder, value);
                modified = true;
            }
        }

        Pattern leftover = Pattern.compile("\\{\\{([^}]+)}}");
        Matcher lm = leftover.matcher(fullText);

        if (lm.find()) {
            do {
                String missingKey = lm.group(1);
                log.warn("Placeholder '{}' not in data map → replaced with fallback", missingKey);
            } while (lm.find());

            fullText = leftover.matcher(fullText).replaceAll(inTable ? "" : ".........");
            modified = true;
        }

        if (!modified) return;

        List<XWPFRun> oldRuns = new ArrayList<>(paragraph.getRuns()); // safe copy
        XWPFRun baseFormat = oldRuns.isEmpty() ? null : oldRuns.get(0);

        // remove all runs safely
        for (int i = paragraph.getRuns().size() - 1; i >= 0; i--) {
            paragraph.removeRun(i);
        }

        // Create one new run
        XWPFRun newRun = paragraph.createRun();
        newRun.setText(fullText);

        if (baseFormat != null) copyRunFormatting(baseFormat, newRun);
    }


    /**
     * PHƯƠNG THỨC MỚI - Copy formatting từ run cũ sang run mới
     */
    private void copyRunFormatting(XWPFRun source, XWPFRun target) {
        try {
            if (source.getFontFamily() != null) target.setFontFamily(source.getFontFamily());
        } catch (Exception ignored) {
        }
        try {
            if (source.getFontSize() != -1) target.setFontSize(source.getFontSize());
        } catch (Exception ignored) {
        }
        try {
            target.setBold(source.isBold());
        } catch (Exception ignored) {
        }
        try {
            target.setItalic(source.isItalic());
        } catch (Exception ignored) {
        }
        try {
            target.setUnderline(source.getUnderline());
        } catch (Exception ignored) {
        }
        try {
            if (source.getColor() != null) target.setColor(source.getColor());
        } catch (Exception ignored) {
        }
    }

    private void removeTable(XWPFDocument doc, XWPFTable table) {
        int index = doc.getTables().indexOf(table);
        if (index >= 0) {
            doc.removeBodyElement(doc.getPosOfTable(table));
            log.info("Removed empty table at index {}", index);
        }
    }

    private XWPFTable findTableByHeader(XWPFDocument doc, String headerText) {
        for (XWPFTable table : doc.getTables()) {
            if (table.getRows().size() > 0 &&
                    table.getRow(0).getTableCells().size() > 0) {

                String txt = table.getRow(0).getCell(0).getText();
                if (txt != null && txt.contains(headerText)) {
                    return table;
                }
            }
        }
        return null;
    }

    private <T> void fillOrRemove(
            String label,
            List<T> list,
            XWPFDocument doc,
            String tableHeaderText,
            java.util.function.Consumer<List<T>> filler
    ) {
        XWPFTable table = findTableByHeader(doc, tableHeaderText);

        if (list == null || list.isEmpty()) {
            log.info("[{}] has NO DATA → removing table [{}]", label, tableHeaderText);
            removeTable(doc, table);
            return;
        }

        log.info("Filling [{}] ({} records)", label, list.size());
        filler.accept(list);
    }

}