package com.EIPplatform.service.report.reportsection;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.mapper.report.ReportSectionMapper;
import com.EIPplatform.model.dto.report.hazardwaste.HazardWasteDTO;
import com.EIPplatform.model.dto.report.monitorexceedance.MonitorExceedanceDTO;
import com.EIPplatform.model.dto.report.reportsection.ReportSectionDTO;
import com.EIPplatform.model.dto.report.reportsection.ReportSectionRequest;
import com.EIPplatform.model.entity.report.ReportSection;
import com.EIPplatform.model.entity.report.Report;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.repository.report.reportsection.ReportSectionRepository;
import com.EIPplatform.repository.report.ReportRepository;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.service.report.hazardwaste.HazardWasteService;
import com.EIPplatform.service.report.monitorexceedance.MonitorExceedanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReportSectionServiceImpl implements ReportSectionService {

    private final ReportSectionRepository reportSectionRepository;
    private final ReportRepository reportRepository;
    private final ReportSectionMapper reportSectionMapper;
    private final HazardWasteService hazardWasteService;
    private final MonitorExceedanceService monitorExceedanceService;
    private final ExceptionFactory exceptionFactory;

    private UserAccount getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw exceptionFactory.createCustomException(ReportError.FORBIDDEN_BUSINESS_ACCESS);
        }
        UserAccount currentUser = (UserAccount) authentication.getPrincipal();
        if (currentUser == null) {
            throw exceptionFactory.createCustomException(ReportError.FORBIDDEN_BUSINESS_ACCESS);
        }
        return currentUser;
    }

    private void validateReportAccess(UUID reportId, UserAccount currentUser) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Report", "id", reportId, ReportError.REPORT_NOT_FOUND));
        if (!report.getBusinessDetail().getUserAccounts().equals(currentUser) && !currentUser.getRoles().stream().anyMatch(r -> "ADMIN".equals(r.getRoleName()))) {
            throw exceptionFactory.createCustomException(ReportError.FORBIDDEN_BUSINESS_ACCESS);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportSectionDTO> findAll() {
        List<ReportSection> entities = reportSectionRepository.findAll();
        return reportSectionMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly =  true)
    public Optional<ReportSectionDTO> findById(UUID id) {
        return reportSectionRepository.findById(id)
                .map(reportSectionMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportSectionDTO> findByReportId(UUID reportId) {
        UserAccount currentUser = getCurrentUser();
        validateReportAccess(reportId, currentUser);
        List<ReportSection> entities = reportSectionRepository.findByReportId(reportId);
        return reportSectionMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportSectionDTO> findByReportIdAndSectionType(UUID reportId, String sectionType) {
        UserAccount currentUser = getCurrentUser();
        validateReportAccess(reportId, currentUser);
        List<ReportSection> entities = reportSectionRepository.findByReportIdAndSectionType(reportId, sectionType);
        return reportSectionMapper.toDTOList(entities);
    }

    @Override
    public ReportSectionDTO createReportSection(ReportSectionRequest request, UUID reportId) {
        UserAccount currentUser = getCurrentUser();
        validateReportAccess(reportId, currentUser);

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Report", "id", reportId, ReportError.REPORT_NOT_FOUND));

        ReportSection entity = reportSectionMapper.toEntity(request);
        entity.setReport(report);
        entity.setVersion(1);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity = reportSectionRepository.save(entity);

        UUID sectionId = entity.getSectionId();
        if (request.getHazardWastes() != null) {
            for (var hwRequest : request.getHazardWastes()) {
                hazardWasteService.createHazardWaste(hwRequest, sectionId);
            }
        }
        if (request.getExceedances() != null) {
            for (var meRequest : request.getExceedances()) {
                monitorExceedanceService.createMonitorExceedance(meRequest, sectionId);
            }
        }

        log.info("ReportSection created successfully for report: {}", reportId);
        return reportSectionMapper.toDTO(entity);
    }

    @Override
    public ReportSectionDTO updateReportSection(UUID id, ReportSectionRequest request) {
        UserAccount currentUser = getCurrentUser();
        ReportSection entity = reportSectionRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportSection", "id", id, ReportError.REPORT_NOT_FOUND));

        validateReportAccess(entity.getReport().getReportId(), currentUser);

        List<HazardWasteDTO> oldHWs = hazardWasteService.findBySectionId(id);
        for (var oldHW : oldHWs) {
            hazardWasteService.deleteById(oldHW.getHwStatId());
        }
        List<MonitorExceedanceDTO> oldMEs = monitorExceedanceService.findBySectionId(id);
        for (var oldME : oldMEs) {
            monitorExceedanceService.deleteById(oldME.getExceedanceId());
        }

        reportSectionMapper.updateEntityFromRequest(request, entity);
        entity.setVersion(entity.getVersion() + 1);
        entity.setUpdatedAt(LocalDateTime.now());
        entity = reportSectionRepository.save(entity);

        if (request.getHazardWastes() != null) {
            for (var hwRequest : request.getHazardWastes()) {
                hazardWasteService.createHazardWaste(hwRequest, id);
            }
        }
        if (request.getExceedances() != null) {
            for (var meRequest : request.getExceedances()) {
                monitorExceedanceService.createMonitorExceedance(meRequest, id);
            }
        }

        log.info("ReportSection updated successfully: {}", id);
        return reportSectionMapper.toDTO(entity);
    }

    @Override
    public void deleteById(UUID id) {
        UserAccount currentUser = getCurrentUser();
        ReportSection entity = reportSectionRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportSection", "id", id, ReportError.REPORT_NOT_FOUND));

        validateReportAccess(entity.getReport().getReportId(), currentUser);

        List<HazardWasteDTO> hws = hazardWasteService.findBySectionId(id);
        for (var hw : hws) {
            hazardWasteService.deleteById(hw.getHwStatId());
        }
        List<MonitorExceedanceDTO> mes = monitorExceedanceService.findBySectionId(id);
        for (var me : mes) {
            monitorExceedanceService.deleteById(me.getExceedanceId());
        }

        reportSectionRepository.deleteById(id);
        log.info("ReportSection deleted successfully: {}", id);
    }
}