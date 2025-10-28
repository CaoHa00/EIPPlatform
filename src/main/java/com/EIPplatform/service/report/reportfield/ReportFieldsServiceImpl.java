package com.EIPplatform.service.report.reportfield;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.mapper.report.ReportFieldsMapper;
import com.EIPplatform.model.dto.report.reportfield.ReportFieldDTO;
import com.EIPplatform.model.dto.report.reportfield.ReportFieldRequest;
import com.EIPplatform.model.entity.report.ReportFields;
import com.EIPplatform.model.entity.report.Report;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.repository.report.reportfield.ReportFieldsRepository;
import com.EIPplatform.repository.report.ReportRepository;
import com.EIPplatform.exception.errorCategories.ReportError;
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
public class ReportFieldsServiceImpl implements ReportFieldsService {

    private final ReportFieldsRepository reportFieldsRepository;
    private final ReportRepository reportRepository;
    private final ReportFieldsMapper reportFieldsMapper;
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
    public List<ReportFieldDTO> findAll() {
        List<ReportFields> entities = reportFieldsRepository.findAll();
        return reportFieldsMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportFieldDTO> findById(Integer id) {
        return reportFieldsRepository.findById(id)
                .map(reportFieldsMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportFieldDTO> findByReportId(UUID reportId) {
        UserAccount currentUser = getCurrentUser();
        validateReportAccess(reportId, currentUser);
        List<ReportFields> entities = reportFieldsRepository.findByReportId(reportId);
        return reportFieldsMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportFieldDTO> findByReportIdAndFieldName(UUID reportId, String fieldName) {
        UserAccount currentUser = getCurrentUser();
        validateReportAccess(reportId, currentUser);
        List<ReportFields> entities = reportFieldsRepository.findByReportIdAndFieldName(reportId, fieldName);
        return reportFieldsMapper.toDTOList(entities);
    }

    @Override
    public ReportFieldDTO createReportField(ReportFieldRequest request, UUID reportId) {
        UserAccount currentUser = getCurrentUser();
        validateReportAccess(reportId, currentUser);

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Report", "id", reportId, ReportError.REPORT_NOT_FOUND));

        ReportFields entity = reportFieldsMapper.toEntity(request);
        entity.setReport(report);
        entity.setCreatedAt(LocalDateTime.now());
        entity = reportFieldsRepository.save(entity);
        log.info("ReportField created successfully for report: {}", reportId);
        return reportFieldsMapper.toDTO(entity);
    }

    @Override
    public ReportFieldDTO updateReportField(Integer id, ReportFieldRequest request) {
        UserAccount currentUser = getCurrentUser();
        ReportFields entity = reportFieldsRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportFields", "id", id, ReportError.REPORT_NOT_FOUND));

        validateReportAccess(entity.getReport().getReportId(), currentUser);

        reportFieldsMapper.updateEntityFromRequest(request, entity);
        entity = reportFieldsRepository.save(entity);
        log.info("ReportField updated successfully: {}", id);
        return reportFieldsMapper.toDTO(entity);
    }

    @Override
    public void deleteById(Integer id) {
        UserAccount currentUser = getCurrentUser();
        ReportFields entity = reportFieldsRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportFields", "id", id, ReportError.REPORT_NOT_FOUND));

        validateReportAccess(entity.getReport().getReportId(), currentUser);

        reportFieldsRepository.deleteById(id);
        log.info("ReportField deleted successfully: {}", id);
    }
}