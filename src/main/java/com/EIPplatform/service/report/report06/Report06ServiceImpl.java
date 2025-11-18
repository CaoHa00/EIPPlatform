package com.EIPplatform.service.report.report06;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.mapper.report.report06.Report06Mapper;
import com.EIPplatform.mapper.report.report06.part02.OperationalActivityDataMapper;
import com.EIPplatform.mapper.report.report06.part03.InventoryResultDataMapper;
import com.EIPplatform.model.dto.report.report06.CreateReportRequest;
import com.EIPplatform.model.dto.report.report06.Report06DTO;
import com.EIPplatform.model.dto.report.report06.Report06DraftDTO;
import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.report.report06.*;
import com.EIPplatform.model.entity.report.report06.part01.BusinessInformation;
import com.EIPplatform.model.entity.report.report06.part02.OperationalActivityData;
import com.EIPplatform.model.entity.report.report06.part03.InventoryResultData;
import com.EIPplatform.repository.businessInformation.BusinessDetailRepository;
import com.EIPplatform.repository.report.report06.*;
import com.EIPplatform.repository.report.report06.part02.OperationalActivityDataRepository;
import com.EIPplatform.repository.report.report06.part03.InventoryResultDataRepository;
import com.EIPplatform.service.report.reportCache.reportCacheA05.ReportCacheFactory;
import com.EIPplatform.service.report.reportCache.reportCacheA05.ReportCacheService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class Report06ServiceImpl implements Report06Service {

    Report06Repository report06Repository;
    BusinessDetailRepository businessDetailRepository;

    // Repository cho 3 phần dữ liệu
    OperationalActivityDataRepository operationalActivityDataRepository;
    InventoryResultDataRepository inventoryResultDataRepository;

    // Mapper cho 3 phần
    OperationalActivityDataMapper operationalActivityDataMapper;
    InventoryResultDataMapper inventoryResultDataMapper;
    Report06Mapper report06Mapper;

    ReportCacheFactory reportCacheFactory;
    ReportCacheService<Report06DraftDTO> reportCacheService;
    ExceptionFactory exceptionFactory;

    @Autowired
    public Report06ServiceImpl(
            Report06Repository report06Repository,
            BusinessDetailRepository businessDetailRepository,
            OperationalActivityDataRepository operationalActivityDataRepository,
            InventoryResultDataRepository inventoryResultDataRepository,
            OperationalActivityDataMapper operationalActivityDataMapper,
            InventoryResultDataMapper inventoryResultDataMapper,
            Report06Mapper report06Mapper,
            ReportCacheFactory reportCacheFactory,
            ExceptionFactory exceptionFactory) {

        this.report06Repository = report06Repository;
        this.businessDetailRepository = businessDetailRepository;
        this.operationalActivityDataRepository = operationalActivityDataRepository;
        this.inventoryResultDataRepository = inventoryResultDataRepository;
        this.operationalActivityDataMapper = operationalActivityDataMapper;
        this.inventoryResultDataMapper = inventoryResultDataMapper;
        this.report06Mapper = report06Mapper;
        this.reportCacheFactory = reportCacheFactory;
        this.exceptionFactory = exceptionFactory;

        this.reportCacheService = reportCacheFactory.getCacheService(Report06DraftDTO.class);
    }

    @Override
    @Transactional
    public Report06DTO createReport(CreateReportRequest request) {
        BusinessDetail businessDetail = businessDetailRepository
                .findById(request.getBusinessDetailId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail", request.getBusinessDetailId(), ReportError.BUSINESS_NOT_FOUND));

        String reportName = "RPT06-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Report06 report = Report06.builder()
                .reportName(reportName)
                .businessDetail(businessDetail)
                .reportYear(request.getReportYear())
                .reportingPeriod(request.getReportingPeriod())
                .version(1)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Report06 saved = report06Repository.save(report);
        return report06Mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Report06DTO getReportById(UUID report06Id) {
        Report06 report = report06Repository.findById(report06Id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Report06", report06Id, ReportError.REPORT_NOT_FOUND));

        return report06Mapper.toDto(report);
    }

    @Override
    public Report06DraftDTO getDraftData(UUID report06Id, UUID userAccountId) {
        return reportCacheService.getDraftReport(report06Id, userAccountId);
    }

    @Override
    @Transactional
    public Report06DTO submitDraftToDatabase(UUID report06Id, UUID userAccountId) {
        Report06DraftDTO draft = getDraftData(report06Id, userAccountId);
        if (draft == null) {
            throw exceptionFactory.createCustomException(ReportError.DRAFT_NOT_FOUND);
        }

        Report06 report = report06Repository.findById(report06Id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Report06", report06Id, ReportError.REPORT_NOT_FOUND));

        // Lưu/cập nhật 3 phần dữ liệu (không kiểm tra hoàn thành nữa)
//        saveOrUpdateBusinessInformation(report, draft);
        saveOrUpdateOperationalActivityData(report, draft);
        saveOrUpdateInventoryResultData(report, draft);

        report.setUpdatedAt(LocalDateTime.now());
        Report06 saved = report06Repository.save(report);

        // Xóa draft sau khi submit thành công
        reportCacheService.deleteDraftReport(report06Id, userAccountId);

        return report06Mapper.toDto(saved);
    }

    // ====================== PRIVATE HELPERS ======================

//    private Report06DTO toDto(Report06 report) {
//        BusinessDetail bd = report.getBusinessDetail();
//
////        var businessInfoDto = report.getBusinessInformation() != null
////                ? businessInformationMapper.toDto(report.getBusinessInformation())
////                : null;
//
//        var operationalDto = report.getOperationalActivityData() != null
//                ? operationalActivityDataMapper.toDTO(report.getOperationalActivityData())
//                : null;
//
//        var inventoryDto = report.getInventoryResultData() != null
//                ? inventoryResultDataMapper.toDTO(report.getInventoryResultData())
//                : null;
//
//        return Report06DTO.builder()
//                .report06Id(report.getReport06Id())
//                .reportCode(report.getReportName())
//                .businessDetailId(bd != null ? bd.getBusinessDetailId() : null)
//                .facilityName(bd != null ? bd.getFacilityName() : null)
//                .reportYear(report.getReportYear())
//                .reportingPeriod(report.getReportingPeriod())
//                .reviewNotes(report.getReviewNotes())
//                .createdAt(report.getCreatedAt())
//                .isDeleted(report.getIsDeleted())
////                .businessInformationDTO(businessInfoDto)
//                .operationalActivityDataDTO(operationalDto)
//                .inventoryResultDataDTO(inventoryDto)
//                .build();
//    }

//    private void saveOrUpdateBusinessInformation(Report06 report, Report06DraftDTO draft) {
//        var dto = draft.getBusinessInformation();
//        if (dto == null) return;
//
//        BusinessInformation entity = report.getBusinessInformation() != null
//                ? report.getBusinessInformation()
//                : new BusinessInformation();
//
//        businessInformationMapper.updateEntityFromDto(dto, entity);
//        entity.setReport06(report);
//        report.setBusinessInformation(entity);
//    }

    private void saveOrUpdateOperationalActivityData(Report06 report, Report06DraftDTO draft) {
        var dto = draft.getOperationalActivityData();
        if (dto == null) return;

        OperationalActivityData entity = report.getOperationalActivityData() != null
                ? report.getOperationalActivityData()
                : new OperationalActivityData();

        operationalActivityDataMapper.updateEntityFromDto(dto, entity);
        entity.setReport06(report);
        report.setOperationalActivityData(entity);
    }

    private void saveOrUpdateInventoryResultData(Report06 report, Report06DraftDTO draft) {
        var dto = draft.getInventoryResultData();
        if (dto == null) return;

        InventoryResultData entity = report.getInventoryResultData() != null
                ? report.getInventoryResultData()
                : new InventoryResultData();

        inventoryResultDataMapper.updateEntityFromDto(dto, entity);
        entity.setReport06(report);
        report.setInventoryResultData(entity);
    }
}