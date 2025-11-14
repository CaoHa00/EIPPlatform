package com.EIPplatform.service.permits;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ForbiddenError;
import com.EIPplatform.exception.errorCategories.PermitError;
import com.EIPplatform.exception.errorCategories.UserError;
import com.EIPplatform.exception.errorCategories.ValidationError;
import com.EIPplatform.mapper.permitshistory.PermitMapper;
import com.EIPplatform.model.dto.fileStorage.FileStorageRequest;
import com.EIPplatform.model.dto.permitshistory.CreateComponentPermitRequest;
import com.EIPplatform.model.dto.permitshistory.CreateMainPermitRequest;
import com.EIPplatform.model.dto.permitshistory.EnvComponentPermitDTO;
import com.EIPplatform.model.dto.permitshistory.EnvPermitDTO;
import com.EIPplatform.model.dto.permitshistory.PermitStatisticsDTO;
import com.EIPplatform.model.dto.permitshistory.UpdateComponentPermitRequest;
import com.EIPplatform.model.dto.permitshistory.UpdateEnvPermitRequest;
import com.EIPplatform.model.entity.permitshistory.EnvPermits;
import com.EIPplatform.model.entity.permitshistory.EnvComponentPermit;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.repository.authentication.UserAccountRepository;
import com.EIPplatform.repository.permitshistory.EnvPermitsRepository;
import com.EIPplatform.repository.user.BusinessDetailRepository;
import com.EIPplatform.repository.permitshistory.EnvComponentPermitRepository;
import com.EIPplatform.service.fileStorage.FileStorageService;
import com.EIPplatform.util.PermitUtils;
import com.EIPplatform.util.StringNormalizerUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermitServiceImpl implements PermitService {

    EnvPermitsRepository envPermitsRepository;
    EnvComponentPermitRepository componentPermitRepository;
    BusinessDetailRepository businessDetailRepository;
    FileStorageService fileStorageService;
    PermitMapper permitMapper;
    ExceptionFactory exceptionFactory;
    UserAccountRepository userAccountRepository;

    @Autowired
    public PermitServiceImpl(
            EnvPermitsRepository envPermitsRepository,
            EnvComponentPermitRepository componentPermitRepository,
            BusinessDetailRepository businessDetailRepository,
            FileStorageService fileStorageService,
            PermitMapper permitMapper,
            UserAccountRepository userAccountRepository,
            ExceptionFactory exceptionFactory) {
        this.envPermitsRepository = envPermitsRepository;
        this.componentPermitRepository = componentPermitRepository;
        this.businessDetailRepository = businessDetailRepository;
        this.fileStorageService = fileStorageService;
        this.permitMapper = permitMapper;
        this.userAccountRepository = userAccountRepository;
        this.exceptionFactory = exceptionFactory;
    }

    // ==================== ENV PERMITS METHODS ====================

    @Override
    public EnvPermitDTO createEnvPermit(UUID userAccountId, CreateMainPermitRequest request, MultipartFile file) {
        request = StringNormalizerUtil.normalizeRequest(request);
        PermitUtils.validateUserExists(userAccountRepository, userAccountId, exceptionFactory);

        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        if (envPermitsRepository.existsByBusinessDetail_BusinessDetailId(businessDetail.getBusinessDetailId())) {
            throw exceptionFactory.createValidationException(
                    "EnvPermit",
                    "Business '" + businessDetail.getFacilityName() + "' already has an environmental permit",
                    "",
                    PermitError.INVALID_OPERATION
            );
        }

        EnvPermits envPermit = EnvPermits.builder()
                .businessDetail(businessDetail)
                .permitNumber(request.getPermitNumber())
                .issueDate(request.getIssueDate())
                .issuerOrg(request.getIssuerOrg())
                .projectName(request.getProjectName())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        if (file != null && !file.isEmpty()) {
            int year = (request.getIssueDate() != null) ? request.getIssueDate().getYear() : LocalDate.now().getYear();
            String filePath = PermitUtils.uploadPermitFile(businessDetail, file, "env-permit", year, fileStorageService, exceptionFactory);
            envPermit.setPermitFilePath(filePath);
        }

        envPermit = envPermitsRepository.save(envPermit);
        log.info("EnvPermit created: {} by user: {}", envPermit.getPermitId(), userAccountId);

        return permitMapper.toMainPermitDTO(envPermit);
    }

    @Override
    @Transactional(readOnly = true)
    public EnvPermitDTO getEnvPermit(UUID userAccountId) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        EnvPermits envPermit = envPermitsRepository
                .findByBusinessDetail_BusinessDetailId(businessDetail.getBusinessDetailId())
                .orElse(null);

        return envPermit != null ? permitMapper.toMainPermitDTO(envPermit) : null;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasEnvPermit(UUID userAccountId) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);
        return envPermitsRepository.existsByBusinessDetail_BusinessDetailId(
                businessDetail.getBusinessDetailId());
    }

    @Override
    public EnvPermitDTO updateEnvPermit(UUID userAccountId, UpdateEnvPermitRequest request, MultipartFile file) {
        request = StringNormalizerUtil.normalizeRequest(request);
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        EnvPermits envPermit = envPermitsRepository
                .findByBusinessDetail_BusinessDetailId(businessDetail.getBusinessDetailId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "EnvPermit", "businessDetailId", businessDetail.getBusinessDetailId(),
                        PermitError.NOT_FOUND));

        // Update metadata
        if (request.getPermitNumber() != null) {
            envPermit.setPermitNumber(request.getPermitNumber());
        }
        if (request.getIssueDate() != null) {
            envPermit.setIssueDate(request.getIssueDate());
        }
        if (request.getIssuerOrg() != null) {
            envPermit.setIssuerOrg(request.getIssuerOrg());
        }
        if (request.getProjectName() != null) {
            envPermit.setProjectName(request.getProjectName());
        }

        if (file != null && !file.isEmpty()) {
            PermitUtils.validatePermitFile(file, exceptionFactory);

            if (envPermit.getPermitFilePath() != null) {
                try {
                    fileStorageService.deleteFile(envPermit.getPermitFilePath());
                } catch (Exception e) {
                    log.warn("Failed to delete old file: {}", envPermit.getPermitFilePath(), e);
                }
            }

            int year = (request.getIssueDate() != null) ? request.getIssueDate().getYear() :
                    (envPermit.getIssueDate() != null ? envPermit.getIssueDate().getYear() : LocalDate.now().getYear());
            String filePath = PermitUtils.uploadPermitFile(businessDetail, file, "env-permit", year, fileStorageService, exceptionFactory);
            envPermit.setPermitFilePath(filePath);
        }

        envPermit = envPermitsRepository.save(envPermit);
        log.info("EnvPermit updated by user: {}", userAccountId);

        return permitMapper.toMainPermitDTO(envPermit);
    }

    @Override
    public void deleteEnvPermit(UUID userAccountId) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        EnvPermits envPermit = envPermitsRepository
                .findByBusinessDetail_BusinessDetailId(businessDetail.getBusinessDetailId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "EnvPermit", "businessDetailId", businessDetail.getBusinessDetailId(),
                        PermitError.NOT_FOUND));

        if (envPermit.getPermitFilePath() != null) {
            try {
                fileStorageService.deleteFile(envPermit.getPermitFilePath());
            } catch (Exception e) {
                log.warn("Failed to delete file: {}", envPermit.getPermitFilePath(), e);
            }
        }

        envPermitsRepository.delete(envPermit);
        log.info("EnvPermit deleted by user: {}", userAccountId);
    }

    @Override
    public void activateEnvPermit(UUID userAccountId) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        EnvPermits envPermit = envPermitsRepository
                .findByBusinessDetail_BusinessDetailId(businessDetail.getBusinessDetailId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "EnvPermit", "businessDetailId", businessDetail.getBusinessDetailId(),
                        PermitError.NOT_FOUND));

        envPermit.setIsActive(true);
        envPermitsRepository.save(envPermit);
        log.info("EnvPermit activated by user: {}", userAccountId);
    }

    @Override
    public void deactivateEnvPermit(UUID userAccountId) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        EnvPermits envPermit = envPermitsRepository
                .findByBusinessDetail_BusinessDetailId(businessDetail.getBusinessDetailId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "EnvPermit", "businessDetailId", businessDetail.getBusinessDetailId(),
                        PermitError.NOT_FOUND));

        envPermit.setIsActive(false);
        envPermitsRepository.save(envPermit);
        log.info("EnvPermit deactivated by user: {}", userAccountId);
    }

    // ==================== COMPONENT PERMIT METHODS ====================

    @Override
    public EnvComponentPermitDTO createComponentPermit(UUID userAccountId, CreateComponentPermitRequest request,
                                                       MultipartFile file) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        PermitUtils.checkDuplicateComponentPermitNumber(componentPermitRepository, businessDetail.getBusinessDetailId(), request.getPermitNumber(), exceptionFactory);

        EnvComponentPermit componentPermit = EnvComponentPermit.builder()
                .businessDetail(businessDetail)
                .permitType(request.getPermitType())
                .projectName(request.getProjectName())
                .permitNumber(request.getPermitNumber())
                .issueDate(request.getIssueDate())
                .issuerOrg(request.getIssuerOrg())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        if (file != null && !file.isEmpty()) {
            int year = (request.getIssueDate() != null) ? request.getIssueDate().getYear() : LocalDate.now().getYear();
            String filePath = PermitUtils.uploadPermitFile(businessDetail, file, "component-permits", year, fileStorageService, exceptionFactory);
            componentPermit.setPermitFilePath(filePath);
        }

        componentPermit = componentPermitRepository.save(componentPermit);
        log.info("Component permit created: {} by user: {}", componentPermit.getPermitId(), userAccountId);

        return permitMapper.toComponentPermitDTO(componentPermit);
    }

    @Override
    @Transactional
    public List<EnvComponentPermitDTO> createComponentPermits(UUID userAccountId, List<CreateComponentPermitRequest> requests, MultipartFile[] files) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        businessDetailRepository.deleteByBusinessDetailBusinessDetailId(businessDetail.getBusinessDetailId());
        log.info("Deleted all existing component permits for businessDetail: {}", businessDetail.getBusinessDetailId());
        List<EnvComponentPermitDTO> results = new ArrayList<>();
        int numFiles = (files != null) ? files.length : 0;

        for (int i = 0; i < requests.size(); i++) {
            CreateComponentPermitRequest request = requests.get(i);
            MultipartFile file = (i < numFiles) ? files[i] : null;

            PermitUtils.checkDuplicateComponentPermitNumber(componentPermitRepository, businessDetail.getBusinessDetailId(), request.getPermitNumber(), exceptionFactory);

            EnvComponentPermit componentPermit = EnvComponentPermit.builder()
                    .businessDetail(businessDetail)
                    .permitType(request.getPermitType())
                    .projectName(request.getProjectName())
                    .permitNumber(request.getPermitNumber())
                    .issueDate(request.getIssueDate())
                    .issuerOrg(request.getIssuerOrg())
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .build();

            if (file != null && !file.isEmpty()) {
                int year = (request.getIssueDate() != null) ? request.getIssueDate().getYear() : LocalDate.now().getYear();
                String filePath = PermitUtils.uploadPermitFile(businessDetail, file, "component-permits", year, fileStorageService, exceptionFactory);
                componentPermit.setPermitFilePath(filePath);
            }

            componentPermit = componentPermitRepository.save(componentPermit);
            log.info("Component permit created: {} by user: {}", componentPermit.getPermitId(), userAccountId);

            results.add(permitMapper.toComponentPermitDTO(componentPermit));
        }

        return results;
    }

    @Override
    public List<EnvComponentPermitDTO> createMultipleComponentPermits(UUID userAccountId,
                                                                      List<CreateComponentPermitRequest> requests) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        if (hasEnvPermit(userAccountId)) {
            throw exceptionFactory.createValidationException(
                    "ComponentPermit",
                    "cannot create component permits when EnvPermit exists",
                    "",
                    ValidationError.INVALID_STATE
            );
        }

        List<String> permitNumbers = requests.stream()
                .map(CreateComponentPermitRequest::getPermitNumber)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Set<String> uniquePermitNumbers = new HashSet<>(permitNumbers);
        if (uniquePermitNumbers.size() < permitNumbers.size()) {
            throw exceptionFactory.createValidationException(
                    "PermitNumbers", "duplicates in batch", "", ValidationError.DUPLICATE_VALUE
            );
        }

        for (String permitNumber : permitNumbers) {
            PermitUtils.checkDuplicateComponentPermitNumber(componentPermitRepository, businessDetail.getBusinessDetailId(), permitNumber, exceptionFactory);
        }

        List<EnvComponentPermit> permits = requests.stream()
                .map(request -> EnvComponentPermit.builder()
                        .businessDetail(businessDetail)
                        .permitType(request.getPermitType())
                        .projectName(request.getProjectName())
                        .permitNumber(request.getPermitNumber())
                        .issueDate(request.getIssueDate())
                        .issuerOrg(request.getIssuerOrg())
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        permits = componentPermitRepository.saveAll(permits);

        log.info("Created {} component permits by user: {}", permits.size(), userAccountId);
        return permits.stream()
                .map(permitMapper::toComponentPermitDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EnvComponentPermitDTO getComponentPermitById(UUID userAccountId, Long permitId) {
        EnvComponentPermit permit = PermitUtils.getComponentPermitAndValidateOwnership(componentPermitRepository, permitId, userAccountId, businessDetailRepository, exceptionFactory);
        return permitMapper.toComponentPermitDTO(permit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnvComponentPermitDTO> getAllComponentPermits(UUID userAccountId) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        List<EnvComponentPermit> permits = componentPermitRepository
                .findByBusinessDetail_BusinessDetailId(businessDetail.getBusinessDetailId());

        return permits.stream()
                .map(permitMapper::toComponentPermitDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnvComponentPermitDTO> getComponentPermitsByType(UUID userAccountId, String permitType) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        List<EnvComponentPermit> permits = componentPermitRepository
                .findByBusinessDetail_BusinessDetailIdAndPermitType(
                        businessDetail.getBusinessDetailId(), permitType);

        return permits.stream()
                .map(permitMapper::toComponentPermitDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnvComponentPermitDTO> getActiveComponentPermits(UUID userAccountId) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        List<EnvComponentPermit> permits = componentPermitRepository
                .findByBusinessDetail_BusinessDetailIdAndIsActive(
                        businessDetail.getBusinessDetailId(), true);

        return permits.stream()
                .map(permitMapper::toComponentPermitDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnvComponentPermitDTO> getInactiveComponentPermits(UUID userAccountId) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        List<EnvComponentPermit> permits = componentPermitRepository
                .findByBusinessDetail_BusinessDetailIdAndIsActive(
                        businessDetail.getBusinessDetailId(), false);

        return permits.stream()
                .map(permitMapper::toComponentPermitDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnvComponentPermitDTO> searchComponentPermits(UUID userAccountId, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Collections.emptyList();
        }

        List<EnvComponentPermit> permits = componentPermitRepository
                .searchPermitsByKeyword(userAccountId, keyword.trim());

        return permits.stream()
                .map(permitMapper::toComponentPermitDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EnvComponentPermitDTO updateComponentPermit(UUID userAccountId, Long permitId,
                                                       UpdateComponentPermitRequest request, MultipartFile file) {
        EnvComponentPermit permit = PermitUtils.getComponentPermitAndValidateOwnership(componentPermitRepository, permitId, userAccountId, businessDetailRepository, exceptionFactory);

        if (request.getPermitNumber() != null &&
                !request.getPermitNumber().equals(permit.getPermitNumber())) {
            PermitUtils.checkDuplicateComponentPermitNumber(
                    componentPermitRepository,
                    permit.getBusinessDetail().getBusinessDetailId(),
                    request.getPermitNumber(),
                    exceptionFactory
            );
        }

        // Update metadata
        if (request.getPermitType() != null) {
            permit.setPermitType(request.getPermitType());
        }
        if (request.getProjectName() != null) {
            permit.setProjectName(request.getProjectName());
        }
        if (request.getPermitNumber() != null) {
            permit.setPermitNumber(request.getPermitNumber());
        }
        if (request.getIssueDate() != null) {
            permit.setIssueDate(request.getIssueDate());
        }
        if (request.getIssuerOrg() != null) {
            permit.setIssuerOrg(request.getIssuerOrg());
        }

        // Handle file update if provided
        if (file != null && !file.isEmpty()) {
            PermitUtils.validatePermitFile(file, exceptionFactory);

            // Delete old file if exists
            if (permit.getPermitFilePath() != null) {
                try {
                    fileStorageService.deleteFile(permit.getPermitFilePath());
                } catch (Exception e) {
                    log.warn("Failed to delete old file: {}", permit.getPermitFilePath(), e);
                }
            }

            // Upload new file with appropriate year
            int year = (request.getIssueDate() != null) ? request.getIssueDate().getYear() :
                    (permit.getIssueDate() != null ? permit.getIssueDate().getYear() : LocalDate.now().getYear());
            String filePath = PermitUtils.uploadPermitFile(permit.getBusinessDetail(), file, "component-permits", year, fileStorageService, exceptionFactory);
            permit.setPermitFilePath(filePath);
        }

        permit = componentPermitRepository.save(permit);
        log.info("Component permit updated: {} by user: {}", permitId, userAccountId);

        return permitMapper.toComponentPermitDTO(permit);
    }

    @Override
    public void activateComponentPermit(UUID userAccountId, Long permitId) {
        EnvComponentPermit permit = PermitUtils.getComponentPermitAndValidateOwnership(componentPermitRepository, permitId, userAccountId, businessDetailRepository, exceptionFactory);
        permit.setIsActive(true);
        componentPermitRepository.save(permit);
        log.info("Component permit activated: {} by user: {}", permitId, userAccountId);
    }

    @Override
    public void deactivateComponentPermit(UUID userAccountId, Long permitId) {
        EnvComponentPermit permit = PermitUtils.getComponentPermitAndValidateOwnership(componentPermitRepository, permitId, userAccountId, businessDetailRepository, exceptionFactory);
        permit.setIsActive(false);
        componentPermitRepository.save(permit);
        log.info("Component permit deactivated: {} by user: {}", permitId, userAccountId);
    }

    @Override
    public void toggleComponentPermitStatus(UUID userAccountId, Long permitId) {
        EnvComponentPermit permit = PermitUtils.getComponentPermitAndValidateOwnership(componentPermitRepository, permitId, userAccountId, businessDetailRepository, exceptionFactory);
        permit.setIsActive(!permit.getIsActive());
        componentPermitRepository.save(permit);
        log.info("Component permit status toggled: {} by user: {}", permitId, userAccountId);
    }

    @Override
    public void activateMultipleComponentPermits(UUID userAccountId, List<Long> permitIds) {
        permitIds.forEach(permitId -> activateComponentPermit(userAccountId, permitId));
    }

    @Override
    public void deactivateMultipleComponentPermits(UUID userAccountId, List<Long> permitIds) {
        permitIds.forEach(permitId -> deactivateComponentPermit(userAccountId, permitId));
    }

    @Override
    public void deleteComponentPermit(UUID userAccountId, Long permitId) {
        EnvComponentPermit permit = PermitUtils.getComponentPermitAndValidateOwnership(componentPermitRepository, permitId, userAccountId, businessDetailRepository, exceptionFactory);

        if (permit.getPermitFilePath() != null) {
            try {
                fileStorageService.deleteFile(permit.getPermitFilePath());
            } catch (Exception e) {
                log.warn("Failed to delete file: {}", permit.getPermitFilePath(), e);
            }
        }

        componentPermitRepository.delete(permit);
        log.info("Component permit deleted: {} by user: {}", permitId, userAccountId);
    }

    @Override
    public void deleteMultipleComponentPermits(UUID userAccountId, List<Long> permitIds) {
        permitIds.forEach(permitId -> deleteComponentPermit(userAccountId, permitId));
    }

    @Override
    public void deleteAllComponentPermits(UUID userAccountId) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        List<EnvComponentPermit> permits = componentPermitRepository
                .findByBusinessDetail_BusinessDetailId(businessDetail.getBusinessDetailId());

        permits.forEach(permit -> {
            if (permit.getPermitFilePath() != null) {
                try {
                    fileStorageService.deleteFile(permit.getPermitFilePath());
                } catch (Exception e) {
                    log.warn("Failed to delete file: {}", permit.getPermitFilePath(), e);
                }
            }
        });

        componentPermitRepository.deleteAll(permits);
        log.info("Deleted {} component permits by user: {}", permits.size(), userAccountId);
    }

    @Override
    public void deleteInactiveComponentPermits(UUID userAccountId) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        List<EnvComponentPermit> inactivePermits = componentPermitRepository
                .findByBusinessDetail_BusinessDetailIdAndIsActive(
                        businessDetail.getBusinessDetailId(), false);

        inactivePermits.forEach(permit -> {
            if (permit.getPermitFilePath() != null) {
                try {
                    fileStorageService.deleteFile(permit.getPermitFilePath());
                } catch (Exception e) {
                    log.warn("Failed to delete file: {}", permit.getPermitFilePath(), e);
                }
            }
        });

        componentPermitRepository.deleteAll(inactivePermits);
        log.info("Deleted {} inactive component permits by user: {}",
                inactivePermits.size(), userAccountId);
    }

    // ==================== FILE METHODS ====================

    @Override
    public EnvPermitDTO uploadEnvPermitFile(UUID userAccountId, MultipartFile file) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        EnvPermits envPermit = envPermitsRepository
                .findByBusinessDetail_BusinessDetailId(businessDetail.getBusinessDetailId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "EnvPermit", "businessDetailId", businessDetail.getBusinessDetailId(),
                        PermitError.NOT_FOUND));

        PermitUtils.validatePermitFile(file, exceptionFactory);

        if (envPermit.getPermitFilePath() != null) {
            try {
                fileStorageService.deleteFile(envPermit.getPermitFilePath());
            } catch (Exception e) {
                log.warn("Failed to delete old file: {}", envPermit.getPermitFilePath(), e);
            }
        }

        int year = (envPermit.getIssueDate() != null) ? envPermit.getIssueDate().getYear() : LocalDate.now().getYear();
        String filePath = PermitUtils.uploadPermitFile(businessDetail, file, "env-permit", year, fileStorageService, exceptionFactory);
        envPermit.setPermitFilePath(filePath);
        envPermit = envPermitsRepository.save(envPermit);

        log.info("EnvPermit file uploaded by user: {}", userAccountId);
        return permitMapper.toMainPermitDTO(envPermit);
    }

    @Override
    public void deleteEnvPermitFile(UUID userAccountId) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        EnvPermits envPermit = envPermitsRepository
                .findByBusinessDetail_BusinessDetailId(businessDetail.getBusinessDetailId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "EnvPermit", "businessDetailId", businessDetail.getBusinessDetailId(),
                        PermitError.NOT_FOUND));

        if (envPermit.getPermitFilePath() == null) {
            throw exceptionFactory.createNotFoundException(
                    "PermitFile", "envPermit", "", PermitError.FILE_NOT_FOUND
            );
        }

        fileStorageService.deleteFile(envPermit.getPermitFilePath());
        envPermit.setPermitFilePath(null);
        envPermitsRepository.save(envPermit);

        log.info("EnvPermit file deleted by user: {}", userAccountId);
    }

    @Override
    public EnvComponentPermitDTO uploadComponentPermitFile(UUID userAccountId, Long permitId, MultipartFile file) {
        EnvComponentPermit permit = PermitUtils.getComponentPermitAndValidateOwnership(componentPermitRepository, permitId, userAccountId, businessDetailRepository, exceptionFactory);
        PermitUtils.validatePermitFile(file, exceptionFactory);

        if (permit.getPermitFilePath() != null) {
            try {
                fileStorageService.deleteFile(permit.getPermitFilePath());
            } catch (Exception e) {
                log.warn("Failed to delete old file: {}", permit.getPermitFilePath(), e);
            }
        }

        int year = (permit.getIssueDate() != null) ? permit.getIssueDate().getYear() : LocalDate.now().getYear();
        String filePath = PermitUtils.uploadPermitFile(permit.getBusinessDetail(), file, "component-permits", year, fileStorageService, exceptionFactory);
        permit.setPermitFilePath(filePath);
        permit = componentPermitRepository.save(permit);

        log.info("Component permit file uploaded: {} by user: {}", permitId, userAccountId);
        return permitMapper.toComponentPermitDTO(permit);
    }

    @Override
    public void deleteComponentPermitFile(UUID userAccountId, Long permitId) {
        EnvComponentPermit permit = PermitUtils.getComponentPermitAndValidateOwnership(componentPermitRepository, permitId, userAccountId, businessDetailRepository, exceptionFactory);

        if (permit.getPermitFilePath() == null) {
            throw exceptionFactory.createNotFoundException(
                    "PermitFile", "permitId", permitId, PermitError.FILE_NOT_FOUND
            );
        }

        fileStorageService.deleteFile(permit.getPermitFilePath());
        permit.setPermitFilePath(null);
        componentPermitRepository.save(permit);

        log.info("Component permit file deleted: {} by user: {}", permitId, userAccountId);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadEnvPermitFile(UUID userAccountId) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);
        EnvPermits envPermit = envPermitsRepository
                .findByBusinessDetail_BusinessDetailId(businessDetail.getBusinessDetailId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "EnvPermit", "businessDetailId", businessDetail.getBusinessDetailId(),
                        PermitError.NOT_FOUND));

        if (envPermit.getPermitFilePath() == null) {
            throw exceptionFactory.createNotFoundException(
                    "PermitFile", "envPermit", "", PermitError.FILE_NOT_FOUND
            );
        }

        return fileStorageService.downloadFile(envPermit.getPermitFilePath());
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadComponentPermitFile(UUID userAccountId, Long permitId) {
        EnvComponentPermit permit = PermitUtils.getComponentPermitAndValidateOwnership(componentPermitRepository, permitId, userAccountId, businessDetailRepository, exceptionFactory);

        if (permit.getPermitFilePath() == null) {
            throw exceptionFactory.createNotFoundException(
                    "PermitFile", "permitId", permitId, PermitError.FILE_NOT_FOUND
            );
        }

        return fileStorageService.downloadFile(permit.getPermitFilePath());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasEnvPermitFile(UUID userAccountId) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        return envPermitsRepository.findByBusinessDetail_BusinessDetailId(businessDetail.getBusinessDetailId())
                .map(permit -> permit.getPermitFilePath() != null &&
                        fileStorageService.fileExists(permit.getPermitFilePath()))
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasComponentPermitFile(UUID userAccountId, Long permitId) {
        EnvComponentPermit permit = PermitUtils.getComponentPermitAndValidateOwnership(componentPermitRepository, permitId, userAccountId, businessDetailRepository, exceptionFactory);
        return permit.getPermitFilePath() != null &&
                fileStorageService.fileExists(permit.getPermitFilePath());
    }

    // ==================== STATISTICS METHODS ====================

    @Override
    @Transactional(readOnly = true)
    public PermitStatisticsDTO getPermitStatistics(UUID userAccountId) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);
        boolean hasEnvPermit = hasEnvPermit(userAccountId);

        long totalComponentPermits = componentPermitRepository
                .countByBusinessDetail_BusinessDetailId(businessDetail.getBusinessDetailId());
        long activeComponentPermits = componentPermitRepository
                .countByBusinessDetail_BusinessDetailIdAndIsActive(
                        businessDetail.getBusinessDetailId(), true);

        Map<String, Long> permitsByType = new HashMap<>();
        if (totalComponentPermits > 0) {
            List<Object[]> typeCounts = componentPermitRepository
                    .countPermitsByType(businessDetail.getBusinessDetailId());
            permitsByType = typeCounts.stream()
                    .collect(Collectors.toMap(
                            obj -> (String) obj[0],
                            obj -> (Long) obj[1]
                    ));
        }

        return PermitStatisticsDTO.builder()
                .hasEnvPermit(hasEnvPermit)
                .totalComponentPermits(totalComponentPermits)
                .activeComponentPermits(activeComponentPermits)
                .inactiveComponentPermits(totalComponentPermits - activeComponentPermits)
                .permitsByType(permitsByType)
                .build();
    }

    // ==================== VALIDATION METHODS ====================

    @Override
    @Transactional(readOnly = true)
    public boolean isComponentPermitNumberUnique(UUID userAccountId, String permitNumber) {
        BusinessDetail businessDetail = PermitUtils.getBusinessDetailByUserAccountId(businessDetailRepository, userAccountId, exceptionFactory);

        return !componentPermitRepository.existsByBusinessDetail_BusinessDetailIdAndPermitNumber(
                businessDetail.getBusinessDetailId(), permitNumber
        );
    }

    @Override
    @Transactional(readOnly = true)
    public void validateComponentPermitOwnership(UUID userAccountId, Long permitId) {
        PermitUtils.getComponentPermitAndValidateOwnership(componentPermitRepository, permitId, userAccountId, businessDetailRepository, exceptionFactory);
    }
}
