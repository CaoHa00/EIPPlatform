package com.EIPplatform.service.report.wastestat;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.mapper.report.wastestat.WasteStatMapper;
import com.EIPplatform.model.dto.report.wastestat.WasteStatDTO;
import com.EIPplatform.model.dto.report.wastestat.WasteStatRequest;
import com.EIPplatform.model.entity.report.WasteStat;
import com.EIPplatform.model.entity.report.Report;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.repository.report.ReportRepository;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.repository.report.wastestat.WasteStatRepository;
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
public class WasteStatServiceImpl implements WasteStatService {

    private final WasteStatRepository wasteStatRepository;
    private final ReportRepository reportRepository;
    private final WasteStatMapper wasteStatMapper;
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
    public List<WasteStatDTO> findAll() {
        List<WasteStat> entities = wasteStatRepository.findAll();
        return wasteStatMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WasteStatDTO> findById(Integer id) {
        return wasteStatRepository.findById(id)
                .map(wasteStatMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WasteStatDTO> findByReportId(UUID reportId) {
        UserAccount currentUser = getCurrentUser();
        validateReportAccess(reportId, currentUser);
        List<WasteStat> entities = wasteStatRepository.findByReportId(reportId);
        return wasteStatMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WasteStatDTO> findByReportIdAndWasteType(UUID reportId, String wasteType) {
        UserAccount currentUser = getCurrentUser();
        validateReportAccess(reportId, currentUser);
        List<WasteStat> entities = wasteStatRepository.findByReportIdAndWasteType(reportId, wasteType);
        return wasteStatMapper.toDTOList(entities);
    }

    @Override
    public WasteStatDTO createWasteStat(WasteStatRequest request, UUID reportId) {
        UserAccount currentUser = getCurrentUser();
        validateReportAccess(reportId, currentUser);

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Report", "id", reportId, ReportError.REPORT_NOT_FOUND));

        WasteStat entity = wasteStatMapper.toEntity(request);
        entity.setReport(report);
        entity = wasteStatRepository.save(entity);
        log.info("WasteStat created successfully for report: {}", reportId);
        return wasteStatMapper.toDTO(entity);
    }

    @Override
    public WasteStatDTO updateWasteStat(Integer id, WasteStatRequest request) {
        UserAccount currentUser = getCurrentUser();
        WasteStat entity = wasteStatRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("WasteStat", "id", id, ReportError.REPORT_NOT_FOUND));

        validateReportAccess(entity.getReport().getReportId(), currentUser);

        wasteStatMapper.updateEntityFromRequest(request, entity);
        entity = wasteStatRepository.save(entity);
        log.info("WasteStat updated successfully: {}", id);
        return wasteStatMapper.toDTO(entity);
    }

    @Override
    public void deleteById(Integer id) {
        UserAccount currentUser = getCurrentUser();
        WasteStat entity = wasteStatRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("WasteStat", "id", id, ReportError.REPORT_NOT_FOUND));

        validateReportAccess(entity.getReport().getReportId(), currentUser);

        wasteStatRepository.deleteById(id);
        log.info("WasteStat deleted successfully: {}", id);
    }
}