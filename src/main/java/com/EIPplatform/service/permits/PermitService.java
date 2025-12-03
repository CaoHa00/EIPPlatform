package com.EIPplatform.service.permits;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.EIPplatform.model.dto.businessInformation.permitshistory.*;

import java.util.List;
import java.util.UUID;

public interface PermitService {

    // ==================== ENV PERMITS ====================
    EnvPermitDTO createEnvPermit(UUID userAccountId, CreateMainPermitRequest request, MultipartFile file);
    EnvPermitDTO getEnvPermit(UUID userAccountId);
    boolean hasEnvPermit(UUID userAccountId);
    EnvPermitDTO updateEnvPermit(UUID userAccountId, UpdateEnvPermitRequest request,MultipartFile file);
    void deleteEnvPermit(UUID userAccountId);
    void activateEnvPermit(UUID userAccountId);
    void deactivateEnvPermit(UUID userAccountId);

    // ==================== COMPONENT PERMITS ====================
    EnvComponentPermitDTO createComponentPermit(UUID userAccountId, CreateComponentPermitRequest request, MultipartFile file);
    List<EnvComponentPermitDTO> createComponentPermits(UUID userAccountId, List<CreateComponentPermitRequest> requests, MultipartFile[] files);
    List<EnvComponentPermitDTO> createMultipleComponentPermits(UUID userAccountId, List<CreateComponentPermitRequest> requests);
    EnvComponentPermitDTO getComponentPermitById(UUID userAccountId, Long permitId);
    List<EnvComponentPermitDTO> getAllComponentPermits(UUID userAccountId);
    List<EnvComponentPermitDTO> getComponentPermitsByType(UUID userAccountId, String permitType);
    List<EnvComponentPermitDTO> getActiveComponentPermits(UUID userAccountId);
    List<EnvComponentPermitDTO> getInactiveComponentPermits(UUID userAccountId);
    List<EnvComponentPermitDTO> searchComponentPermits(UUID userAccountId, String keyword);
    EnvComponentPermitDTO updateComponentPermit(UUID userAccountId, Long permitId, UpdateComponentPermitRequest request,MultipartFile file);
    void activateComponentPermit(UUID userAccountId, Long permitId);
    void deactivateComponentPermit(UUID userAccountId, Long permitId);
    void toggleComponentPermitStatus(UUID userAccountId, Long permitId);
    void activateMultipleComponentPermits(UUID userAccountId, List<Long> permitIds);
    void deactivateMultipleComponentPermits(UUID userAccountId, List<Long> permitIds);
    void deleteComponentPermit(UUID userAccountId, Long permitId);
    void deleteMultipleComponentPermits(UUID userAccountId, List<Long> permitIds);
    void deleteAllComponentPermits(UUID userAccountId);
    void deleteInactiveComponentPermits(UUID userAccountId);

    // ==================== FILE OPERATIONS ====================
    EnvPermitDTO uploadEnvPermitFile(UUID userAccountId, MultipartFile file);
    void deleteEnvPermitFile(UUID userAccountId);
    EnvComponentPermitDTO uploadComponentPermitFile(UUID userAccountId, Long permitId, MultipartFile file);
    void deleteComponentPermitFile(UUID userAccountId, Long permitId);
    Resource downloadEnvPermitFile(UUID userAccountId);
    Resource downloadComponentPermitFile(UUID userAccountId, Long permitId);
    boolean hasEnvPermitFile(UUID userAccountId);
    boolean hasComponentPermitFile(UUID userAccountId, Long permitId);

    // ==================== STATISTICS ====================
    PermitStatisticsDTO getPermitStatistics(UUID userAccountId);

    // ==================== VALIDATION ====================
    boolean isComponentPermitNumberUnique(UUID userAccountId, String permitNumber);
    void validateComponentPermitOwnership(UUID userAccountId, Long permitId);
}