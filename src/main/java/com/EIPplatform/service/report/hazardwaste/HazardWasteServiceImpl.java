package com.EIPplatform.service.report.hazardwaste;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.mapper.report.hazardwaste.HazardWasteMapper;
import com.EIPplatform.model.dto.report.hazardwaste.HazardWasteDTO;
import com.EIPplatform.model.dto.report.hazardwaste.HazardWasteRequest;
import com.EIPplatform.model.entity.report.HazardWaste;
import com.EIPplatform.model.entity.report.Report;
import com.EIPplatform.model.entity.report.ReportSection;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.repository.report.ReportRepository;
import com.EIPplatform.repository.report.reportsection.ReportSectionRepository;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.repository.report.hazardwaste.HazardWasteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class HazardWasteServiceImpl implements HazardWasteService {

    private final HazardWasteRepository hazardWasteRepository;
    private final ReportRepository reportRepository;
    private final ReportSectionRepository reportSectionRepository;
    private final HazardWasteMapper hazardWasteMapper;
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
    public List<HazardWasteDTO> findAll() {
        List<HazardWaste> entities = hazardWasteRepository.findAll();
        return hazardWasteMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HazardWasteDTO> findById(Integer id) {
        return hazardWasteRepository.findById(id)
                .map(hazardWasteMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HazardWasteDTO> findBySectionId(UUID sectionId) {
        getCurrentUser();
        validateSectionAccess(sectionId, getCurrentUser());
        List<HazardWaste> entities = hazardWasteRepository.findByReportSectionSectionId(sectionId);
        return hazardWasteMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HazardWasteDTO> findByReportId(UUID reportId) {
        getCurrentUser();
        validateReportAccess(reportId, getCurrentUser());
        List<HazardWaste> entities = hazardWasteRepository.findByReportId(reportId);
        return hazardWasteMapper.toDTOList(entities);
    }

    @Override
    public HazardWasteDTO createHazardWaste(HazardWasteRequest request, UUID sectionId) {
        UserAccount currentUser = getCurrentUser();
        validateSectionAccess(sectionId, currentUser);

        ReportSection section = reportSectionRepository.findById(sectionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportSection", "id", sectionId, ReportError.REPORT_NOT_FOUND));

        HazardWaste entity = hazardWasteMapper.toEntity(request);
        entity.setReportSection(section);
        entity.setReport(section.getReport());
        entity = hazardWasteRepository.save(entity);
        log.info("HazardWaste created successfully for section: {}", sectionId);
        return hazardWasteMapper.toDTO(entity);
    }

    @Override
    public HazardWasteDTO updateHazardWaste(Integer id, HazardWasteRequest request) {
        UserAccount currentUser = getCurrentUser();
        HazardWaste entity = hazardWasteRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("HazardWaste", "id", id, ReportError.REPORT_NOT_FOUND));

        validateSectionAccess(entity.getReportSection().getSectionId(), currentUser);

        hazardWasteMapper.updateEntityFromRequest(request, entity);
        entity = hazardWasteRepository.save(entity);
        log.info("HazardWaste updated successfully: {}", id);
        return hazardWasteMapper.toDTO(entity);
    }

    @Override
    public void deleteById(Integer id) {
        UserAccount currentUser = getCurrentUser();
        HazardWaste entity = hazardWasteRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("HazardWaste", "id", id, ReportError.REPORT_NOT_FOUND));

        validateSectionAccess(entity.getReportSection().getSectionId(), currentUser);

        hazardWasteRepository.deleteById(id);
        log.info("HazardWaste deleted successfully: {}", id);
    }
}