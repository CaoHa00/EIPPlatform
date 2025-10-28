package com.EIPplatform.service.report.reportfile;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.mapper.report.ReportFileMapper;
import com.EIPplatform.model.dto.report.reportfile.ReportFileDTO;
import com.EIPplatform.model.dto.report.reportfile.ReportFileRequest;
import com.EIPplatform.model.entity.report.ReportFile;
import com.EIPplatform.model.entity.report.Report;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.repository.report.reportfile.ReportFileRepository;
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
public class ReportFileServiceImpl implements ReportFileService {

    private final ReportFileRepository reportFileRepository;
    private final ReportRepository reportRepository;
    private final ReportFileMapper reportFileMapper;
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

    @Override
    @Transactional(readOnly = true)
    public List<ReportFileDTO> findAll() {
        List<ReportFile> entities = reportFileRepository.findAll();
        return reportFileMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportFileDTO> findById(Integer id) {
        return reportFileRepository.findById(id)
                .map(reportFileMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportFileDTO> findByReportId(UUID reportId) {
        UserAccount currentUser = getCurrentUser();
        validateReportAccess(reportId, currentUser);
        List<ReportFile> entities = reportFileRepository.findByReportId(reportId);
        return reportFileMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportFileDTO> findByReportIdAndFileName(UUID reportId, String fileName) {
        UserAccount currentUser = getCurrentUser();
        validateReportAccess(reportId, currentUser);
        ReportFile entity = reportFileRepository.findByReportIdAndFileName(reportId, fileName);
        return Optional.ofNullable(entity)
                .map(reportFileMapper::toDTO);
    }


    @Override
    public ReportFileDTO createReportFile(ReportFileRequest request, UUID reportId) {
        UserAccount currentUser = getCurrentUser();
        validateReportAccess(reportId, currentUser);

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Report", "id", reportId, ReportError.REPORT_NOT_FOUND));

        ReportFile entity = reportFileMapper.toEntity(request);
        entity.setReport(report);
        entity.setUploadDate(LocalDateTime.now());
        entity = reportFileRepository.save(entity);
        log.info("ReportFile created successfully for report: {}", reportId);
        return reportFileMapper.toDTO(entity);
    }

    @Override
    public ReportFileDTO updateReportFile(Integer id, ReportFileRequest request) {
        UserAccount currentUser = getCurrentUser();
        ReportFile entity = reportFileRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportFile", "id", id, ReportError.REPORT_NOT_FOUND));

        validateReportAccess(entity.getReport().getReportId(), currentUser);

        reportFileMapper.updateEntityFromRequest(request, entity);
        entity = reportFileRepository.save(entity);
        log.info("ReportFile updated successfully: {}", id);
        return reportFileMapper.toDTO(entity);
    }

    @Override
    public void deleteById(Integer id) {
        UserAccount currentUser = getCurrentUser();
        ReportFile entity = reportFileRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportFile", "id", id, ReportError.REPORT_NOT_FOUND));

        validateReportAccess(entity.getReport().getReportId(), currentUser);

        reportFileRepository.deleteById(id);
        log.info("ReportFile deleted successfully: {}", id);
    }
}