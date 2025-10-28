package com.EIPplatform.service.report;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.mapper.report.ReportTypeMapper;
import com.EIPplatform.model.dto.report.reporttype.ReportTypeDTO;
import com.EIPplatform.model.dto.report.reporttype.ReportTypeRequest;
import com.EIPplatform.model.entity.report.ReportType;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.repository.report.ReportTypeRepository;
import com.EIPplatform.exception.errorCategories.ReportError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReportTypeServiceImpl implements ReportTypeService {

    private final ReportTypeRepository reportTypeRepository;
    private final ReportTypeMapper reportTypeMapper;
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
    public List<ReportTypeDTO> findAll() {
        getCurrentUser(); // Ensure authenticated
        List<ReportType> entities = reportTypeRepository.findAll();
        return reportTypeMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportTypeDTO> findById(Integer id) {
        getCurrentUser(); // Ensure authenticated
        return reportTypeRepository.findById(id)
                .map(reportTypeMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportTypeDTO> findByDueDateAfter(LocalDate dueDate) {
        getCurrentUser(); // Ensure authenticated
        List<ReportType> entities = reportTypeRepository.findByDueDateAfter(dueDate);
        return reportTypeMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportTypeDTO> findByReportName(String reportName) {
        getCurrentUser(); // Ensure authenticated
        ReportType entity = reportTypeRepository.findByReportName(reportName);
        return Optional.ofNullable(entity).map(reportTypeMapper::toDTO);
    }

    @Override
    public ReportTypeDTO createReportType(ReportTypeRequest request) {
        UserAccount currentUser = getCurrentUser();
        validateAdminAccess(currentUser);

        ReportType entity = reportTypeMapper.toEntity(request);
        entity = reportTypeRepository.save(entity);
        log.info("ReportType created successfully: {}", request.getReportName());
        return reportTypeMapper.toDTO(entity);
    }

    @Override
    public ReportTypeDTO updateReportType(Integer id, ReportTypeRequest request) {
        UserAccount currentUser = getCurrentUser();
        validateAdminAccess(currentUser);

        ReportType entity = reportTypeRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportType", "id", id, ReportError.REPORT_TYPE_NOT_FOUND));

        reportTypeMapper.updateEntityFromRequest(request, entity);
        entity = reportTypeRepository.save(entity);
        log.info("ReportType updated successfully: {}", id);
        return reportTypeMapper.toDTO(entity);
    }

    @Override
    public void deleteById(Integer id) {
        UserAccount currentUser = getCurrentUser();
        validateAdminAccess(currentUser);

        ReportType entity = reportTypeRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportType", "id", id, ReportError.REPORT_TYPE_NOT_FOUND));

        reportTypeRepository.deleteById(id);
        log.info("ReportType deleted successfully: {}", id);
    }
}