package com.EIPplatform.service.report.monitorexceedance;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.mapper.report.monitorexceedance.MonitorExceedanceMapper;
import com.EIPplatform.model.dto.report.monitorexceedance.MonitorExceedanceDTO;
import com.EIPplatform.model.dto.report.monitorexceedance.MonitorExceedanceRequest;
import com.EIPplatform.model.entity.report.MonitorExceedance;
import com.EIPplatform.model.entity.report.Report;
import com.EIPplatform.model.entity.report.ReportSection;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.repository.report.ReportRepository;
import com.EIPplatform.repository.report.reportsection.ReportSectionRepository;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.repository.report.monitorexceedance.MonitorExceedanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MonitorExceedanceServiceImpl implements MonitorExceedanceService {

    private final MonitorExceedanceRepository monitorExceedanceRepository;
    private final ReportRepository reportRepository;
    private final ReportSectionRepository reportSectionRepository;
    private final MonitorExceedanceMapper monitorExceedanceMapper;
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
        if (!report.getBusinessDetail().getUserAccounts().contains(currentUser) && !currentUser.getRoles().stream().anyMatch(r -> "ADMIN".equals(r.getRoleName()))) {
            throw exceptionFactory.createCustomException(ReportError.FORBIDDEN_BUSINESS_ACCESS);
        }
    }

    private void validateSectionAccess(UUID sectionId, UserAccount currentUser) {
        ReportSection section = reportSectionRepository.findById(sectionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportSection", "id", sectionId, ReportError.REPORT_NOT_FOUND));
        validateReportAccess(section.getReport().getReportId(), currentUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonitorExceedanceDTO> findAll() {
        List<MonitorExceedance> entities = monitorExceedanceRepository.findAll();
        return monitorExceedanceMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MonitorExceedanceDTO> findById(Integer id) {
        return monitorExceedanceRepository.findById(id)
                .map(monitorExceedanceMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonitorExceedanceDTO> findBySectionId(UUID sectionId) {
        UserAccount currentUser = getCurrentUser();
        validateSectionAccess(sectionId, currentUser);
        List<MonitorExceedance> entities = monitorExceedanceRepository.findByReportSectionSectionId(sectionId);
        return monitorExceedanceMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonitorExceedanceDTO> findByReportId(UUID reportId) {
        UserAccount currentUser = getCurrentUser();
        validateReportAccess(reportId, currentUser);
        List<MonitorExceedance> entities = monitorExceedanceRepository.findByReportId(reportId);
        return monitorExceedanceMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonitorExceedanceDTO> findBySectionIdAndDate(UUID sectionId, LocalDate date) {
        UserAccount currentUser = getCurrentUser();
        validateSectionAccess(sectionId, currentUser);
        List<MonitorExceedance> entities = monitorExceedanceRepository.findByReportSectionSectionIdAndMonitoringDate(sectionId, date);
        return monitorExceedanceMapper.toDTOList(entities);
    }

    @Override
    public MonitorExceedanceDTO createMonitorExceedance(MonitorExceedanceRequest request, UUID sectionId) {
        UserAccount currentUser = getCurrentUser();
        validateSectionAccess(sectionId, currentUser);

        ReportSection section = reportSectionRepository.findById(sectionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportSection", "id", sectionId, ReportError.REPORT_NOT_FOUND));

        MonitorExceedance entity = monitorExceedanceMapper.toEntity(request);
        entity.setReportSection(section);
        entity.setReport(section.getReport());
        entity = monitorExceedanceRepository.save(entity);
        log.info("MonitorExceedance created successfully for section: {}", sectionId);
        return monitorExceedanceMapper.toDTO(entity);
    }

    @Override
    public MonitorExceedanceDTO updateMonitorExceedance(Integer id, MonitorExceedanceRequest request) {
        UserAccount currentUser = getCurrentUser();
        MonitorExceedance entity = monitorExceedanceRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("MonitorExceedance", "id", id, ReportError.REPORT_NOT_FOUND));

        validateSectionAccess(entity.getReportSection().getSectionId(), currentUser);

        monitorExceedanceMapper.updateEntityFromRequest(request, entity);
        entity = monitorExceedanceRepository.save(entity);
        log.info("MonitorExceedance updated successfully: {}", id);
        return monitorExceedanceMapper.toDTO(entity);
    }

    @Override
    public void deleteById(Integer id) {
        UserAccount currentUser = getCurrentUser();
        MonitorExceedance entity = monitorExceedanceRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("MonitorExceedance", "id", id, ReportError.REPORT_NOT_FOUND));

        validateSectionAccess(entity.getReportSection().getSectionId(), currentUser);

        monitorExceedanceRepository.deleteById(id);
        log.info("MonitorExceedance deleted successfully: {}", id);
    }
}