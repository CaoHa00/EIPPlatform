package com.EIPplatform.service.report.reporttemplate;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.mapper.report.ReportTemplateMapper;
import com.EIPplatform.model.dto.report.reporttemplate.ReportTemplateDTO;
import com.EIPplatform.model.dto.report.reporttemplate.ReportTemplateRequest;
import com.EIPplatform.model.entity.report.ReportTemplate;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.repository.report.reporttemplate.ReportTemplateRepository;
import com.EIPplatform.exception.errorCategories.ReportError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReportTemplateServiceImpl implements ReportTemplateService {

    private final ReportTemplateRepository reportTemplateRepository;
    private final ReportTemplateMapper reportTemplateMapper;
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

    private void validateAdminAccess(UserAccount currentUser) {
        if (!currentUser.getRoles().stream().anyMatch(r -> "ADMIN".equals(r.getRoleName()))) {
            throw exceptionFactory.createCustomException(ReportError.FORBIDDEN_BUSINESS_ACCESS);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportTemplateDTO> findAll() {
        getCurrentUser(); // Ensure authenticated
        List<ReportTemplate> entities = reportTemplateRepository.findAll();
        return reportTemplateMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportTemplateDTO> findById(Integer id) {
        getCurrentUser(); // Ensure authenticated
        return reportTemplateRepository.findById(id)
                .map(reportTemplateMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportTemplateDTO> findByTemplateCode(String templateCode) {
        getCurrentUser(); // Ensure authenticated
        return reportTemplateRepository.findByTemplateCode(templateCode)
                .map(reportTemplateMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportTemplateDTO> findActiveTemplates() {
        getCurrentUser(); // Ensure authenticated
        List<ReportTemplate> entities = reportTemplateRepository.findByIsActiveTrue();
        return reportTemplateMapper.toDTOList(entities);
    }

    @Override
    public ReportTemplateDTO createReportTemplate(ReportTemplateRequest request) {
        UserAccount currentUser = getCurrentUser();
        validateAdminAccess(currentUser);

        ReportTemplate entity = reportTemplateMapper.toEntity(request);
        entity = reportTemplateRepository.save(entity);
        log.info("ReportTemplate created successfully: {}", request.getTemplateCode());
        return reportTemplateMapper.toDTO(entity);
    }

    @Override
    public ReportTemplateDTO updateReportTemplate(Integer id, ReportTemplateRequest request) {
        UserAccount currentUser = getCurrentUser();
        validateAdminAccess(currentUser);

        ReportTemplate entity = reportTemplateRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportTemplate", "id", id, ReportError.REPORT_NOT_FOUND));

        reportTemplateMapper.updateEntityFromRequest(request, entity);
        entity = reportTemplateRepository.save(entity);
        log.info("ReportTemplate updated successfully: {}", id);
        return reportTemplateMapper.toDTO(entity);
    }

    @Override
    public void deleteById(Integer id) {
        UserAccount currentUser = getCurrentUser();
        validateAdminAccess(currentUser);

        ReportTemplate entity = reportTemplateRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportTemplate", "id", id, ReportError.REPORT_NOT_FOUND));

        reportTemplateRepository.deleteById(id);
        log.info("ReportTemplate deleted successfully: {}", id);
    }
}