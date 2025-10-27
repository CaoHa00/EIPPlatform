package com.EIPplatform.service.permitshistory;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ForbiddenError;
import com.EIPplatform.exception.errorCategories.PermitError;
import com.EIPplatform.exception.errorCategories.UserError;
import com.EIPplatform.exception.errorCategories.ValidationError;
import com.EIPplatform.mapper.permitshistory.PermitMapper;
import com.EIPplatform.model.dto.permitshistory.CreatePermitRequest;
import com.EIPplatform.model.dto.permitshistory.EnvPermitDTO;
import com.EIPplatform.model.dto.permitshistory.PermitFilterRequest;
import com.EIPplatform.model.dto.permitshistory.UpdatePermitRequest;
import com.EIPplatform.model.entity.permitsHistory.EnvPermits;
import com.EIPplatform.model.entity.user.userInformation.UserDetail;
import com.EIPplatform.repository.permitshistory.EnvPermitsRepository;
import com.EIPplatform.repository.user.UserDetailRepository;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermitServiceImpl implements PermitService {

    EnvPermitsRepository permitsRepository;
    UserDetailRepository userDetailRepository;
    StorageService storageService;
    PermitMapper permitMapper;
    AuditService auditService;
    ExceptionFactory exceptionFactory;

    @Autowired
    public PermitServiceImpl(
            EnvPermitsRepository permitsRepository,
            UserDetailRepository userDetailRepository,
            StorageService storageService,
            PermitMapper permitMapper,
            AuditService auditService,
            ExceptionFactory exceptionFactory) {
        this.permitsRepository = permitsRepository;
        this.userDetailRepository = userDetailRepository;
        this.storageService = storageService;
        this.permitMapper = permitMapper;
        this.auditService = auditService;
        this.exceptionFactory = exceptionFactory;
    }

    @Override
    public EnvPermitDTO createPermit(UUID userId, CreatePermitRequest request, MultipartFile file) {

        UserDetail userDetail = userDetailRepository.findByUserAccount_UserAccountId(userId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("UserDetail", "userId", userId, UserError.NOT_FOUND));

        EnvPermits permit = permitMapper.toEntity(request, userDetail);

        if (file != null && !file.isEmpty()) {
            validatePermitFile(file);
            String filePath = storageService.uploadFile(file, "permits");
            permit.setPermitFilePath(filePath);
        }

        permit = permitsRepository.save(permit);

        auditService.logAction(userId, "CREATE", "PERMIT",
                permit.getPermitId().toString(),
                "Created permit: " + permit.getPermitNumber());

        log.info("Permit created successfully: {}", permit.getPermitId());
        return permitMapper.toDTO(permit);
    }

    @Override
    public EnvPermitDTO updatePermit(UUID userId, Long permitId, UpdatePermitRequest request) {
        log.info("Updating permit: {} for user: {}", permitId, userId);

        EnvPermits permit = getPermitAndValidateOwnership(permitId, userId);

        String oldValues = String.format("Type: %s, Number: %s",
                permit.getPermitType(), permit.getPermitNumber());

        permitMapper.updateEntityFromDTO(request, permit);
        permit = permitsRepository.save(permit);

        auditService.logAction(userId, "UPDATE", "PERMIT",
                permitId.toString(),
                "Updated from: " + oldValues);

        log.info("Permit updated successfully: {}", permitId);
        return permitMapper.toDTO(permit);
    }

    @Override
    public EnvPermitDTO uploadPermitFile(UUID userId, Long permitId, MultipartFile file) {
        log.info("Uploading file for permit: {}", permitId);

        EnvPermits permit = getPermitAndValidateOwnership(permitId, userId);

        validatePermitFile(file);

        if (permit.getPermitFilePath() != null) {
            storageService.deleteFile(permit.getPermitFilePath());
        }

        String filePath = storageService.uploadFile(file, "permits");
        permit.setPermitFilePath(filePath);
        permit = permitsRepository.save(permit);

        auditService.logAction(userId, "UPDATE", "PERMIT",
                permitId.toString(), "Uploaded permit file");

        log.info("Permit file uploaded successfully: {}", permitId);
        return permitMapper.toDTO(permit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnvPermitDTO> getPermitsByUser(UUID userId, PermitFilterRequest filter) {
        log.info("Getting permits for user: {}", userId);

        List<EnvPermits> permitList = permitsRepository.findByUserAndFilters(
                userId,
                filter.getPermitType(),
                filter.getIsActive(),
                filter.getIssueDateFrom(),
                filter.getIssueDateTo()
        );

        return permitList.stream()
                .map(permitMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EnvPermitDTO getPermitById(UUID userId, Long permitId) {
        log.info("Getting permit: {} for user: {}", permitId, userId);

        EnvPermits permit = getPermitAndValidateOwnership(permitId, userId);
        return permitMapper.toDTO(permit);
    }

    @Override
    public void deactivatePermit(UUID userId, Long permitId) {
        log.info("Deactivating permit: {}", permitId);

        EnvPermits permit = getPermitAndValidateOwnership(permitId, userId);
        permit.setIsActive(false);
        permitsRepository.save(permit);

        auditService.logAction(userId, "UPDATE", "PERMIT",
                permitId.toString(), "Deactivated permit");

        log.info("Permit deactivated successfully: {}", permitId);
    }

    @Override
    public void activatePermit(UUID userId, Long permitId) {
        log.info("Activating permit: {}", permitId);

        EnvPermits permit = getPermitAndValidateOwnership(permitId, userId);
        permit.setIsActive(true);
        permitsRepository.save(permit);

        auditService.logAction(userId, "UPDATE", "PERMIT",
                permitId.toString(), "Activated permit");

        log.info("Permit activated successfully: {}", permitId);
    }

    @Override
    public void deletePermit(UUID userId, Long permitId) {
        log.info("Deleting permit: {}", permitId);

        EnvPermits permit = getPermitAndValidateOwnership(permitId, userId);

        if (permit.getPermitFilePath() != null) {
            storageService.deleteFile(permit.getPermitFilePath());
        }

        permitsRepository.delete(permit);

        auditService.logAction(userId, "DELETE", "PERMIT",
                permitId.toString(), "Deleted permit");

        log.info("Permit deleted successfully: {}", permitId);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadPermitFile(UUID userId, Long permitId) {
        log.info("Downloading permit file: {}", permitId);

        EnvPermits permit = getPermitAndValidateOwnership(permitId, userId);

        if (permit.getPermitFilePath() == null) {
            throw exceptionFactory.createNotFoundException("PermitFile", "permitId", permitId, PermitError.FILE_NOT_FOUND);
        }

        auditService.logAction(userId, "DOWNLOAD", "PERMIT",
                permitId.toString(), "Downloaded permit file");

        return (Resource) storageService.downloadFile(permit.getPermitFilePath());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnvPermitDTO> getExpiringPermits(UUID userId, int daysThreshold) {
        log.info("Getting expiring permits for user: {} with threshold: {} days",
                userId, daysThreshold);

        LocalDate thresholdDate = LocalDate.now().minusYears(5).plusDays(daysThreshold);

        List<EnvPermits> expiringPermits = permitsRepository.findExpiringPermits(userId, thresholdDate);

        return expiringPermits.stream()
                .map(permitMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnvPermitDTO> getAllPermitsByUser(UUID userId) {
        log.info("Getting all permits for user: {}", userId);

        UserDetail userDetail = userDetailRepository.findByUserAccount_UserAccountId(userId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("UserDetail", "userId", userId, UserError.NOT_FOUND));

        List<EnvPermits> permits = permitsRepository.findByUserDetail_UserDetailId(
                userDetail.getUserDetailId()
        );

        return permits.stream()
                .map(permitMapper::toDTO)
                .collect(Collectors.toList());
    }


    private EnvPermits getPermitAndValidateOwnership(Long permitId, UUID userId) {
        EnvPermits permit = permitsRepository.findById(permitId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("EnvPermits", "permitId", permitId, PermitError.NOT_FOUND));

        if (!permit.getUserDetail().getUserAccount().getUserAccountId().equals(userId)) {
            throw exceptionFactory.createCustomException("Permit", Arrays.asList("accessReason"), Arrays.asList("You do not have access to this license"), ForbiddenError.FORBIDDEN);
        }

        return permit;
    }

    private void validatePermitFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw exceptionFactory.createValidationException("File", "empty", true, ValidationError.EMPTY_FILE);
        }

        String contentType = file.getContentType();
        if (!"application/pdf".equals(contentType)) {
            throw exceptionFactory.createValidationException("File", "contentType", contentType, ValidationError.INVALID_TYPE);
        }

        long maxSize = 10 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw exceptionFactory.createValidationException("File", "size", file.getSize(), ValidationError.TOO_LARGE);
        }
    }
}