
package com.EIPplatform.service.businessInformation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.BusinessDetailError;
import com.EIPplatform.exception.errorCategories.UserError;
import com.EIPplatform.mapper.businessInformation.BusinessDetailMapper;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailResponse;
import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.businessInformation.Equipment;
import com.EIPplatform.model.entity.businessInformation.Facility;
import com.EIPplatform.model.entity.businessInformation.Process;
import com.EIPplatform.model.entity.businessInformation.Project;
import com.EIPplatform.model.entity.businessInformation.legalRepresentative.LegalRepresentative;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.repository.authentication.UserAccountRepository;
import com.EIPplatform.repository.businessInformation.BusinessDetailRepository;
import com.EIPplatform.service.fileStorage.FileStorageService;
import com.EIPplatform.util.BusinessDetailUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BusinessDetailImplementation implements BusinessDetailInterface {

    BusinessDetailRepository businessDetailRepository;
    BusinessDetailMapper businessDetailMapper;
    UserAccountRepository userAccountRepository;
    FileStorageService fileStorageService;
    ExceptionFactory exceptionFactory;
    BusinessDetailUtils businessDetailUtils;

    @Override
    public BusinessDetailResponse findByUserAccountId(UUID userAccountId) {
        return businessDetailRepository.findByUserAccountId(userAccountId)
                .map(businessDetailMapper::toResponse)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        "userAccountId",
                        userAccountId,
                        BusinessDetailError.NOT_FOUND));
    }

    @Override
    public void deleteByUserAccountId(UUID userAccountId) {
        BusinessDetail entity = businessDetailRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        "userAccountId",
                        userAccountId,
                        BusinessDetailError.NOT_FOUND));

        UserAccount userAccount = userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "UserAccount",
                        "userAccountId",
                        userAccountId,
                        UserError.NOT_FOUND));
        if (!entity.getUserAccounts().contains(userAccount)) {
            throw exceptionFactory.createCustomException(
                    "BusinessDetail",
                    List.of("userAccountId", "businessDetailId"),
                    List.of(userAccountId, entity.getBusinessDetailId()),
                    UserError.UNAUTHORIZED_ACCESS);
        }
        if (entity.getIsoCertificateFilePath() != null) {
            try {
                fileStorageService.deleteFile(entity.getIsoCertificateFilePath());
                log.info("Deleted ISO cert file for BusinessDetail: {}", entity.getIsoCertificateFilePath());
            } catch (Exception e) {
                log.warn("Failed to delete ISO cert file: {}", entity.getIsoCertificateFilePath(), e);
            }
        }
        for (UserAccount ua : entity.getUserAccounts()) {
            ua.setBusinessDetail(null);
        }
        userAccountRepository.saveAll(entity.getUserAccounts());
        businessDetailRepository.delete(entity);
        log.info("Deleted BusinessDetail - ID: {} for UserAccount: {}", entity.getBusinessDetailId(), userAccountId);
    }

    @Override
    public BusinessDetailResponse createBusinessDetail(
            UUID userAccountId,
            BusinessDetailDTO dto,
            MultipartFile isoFile) {

        businessDetailUtils.validateOperationDetails(dto.getOperationType(), dto.getSeasonalDescription());
        businessDetailUtils.validateUniqueFields(dto, null);

        UserAccount userAccount = userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "UserAccount", "userAccountId", userAccountId, UserError.NOT_FOUND));

        BusinessDetail entity = businessDetailMapper.toEntity(dto);

        // LegalRepresentative
        LegalRepresentative legalRep = businessDetailUtils.fetchLegalRepresentative(dto.getLegalRepresentative());
        entity.setLegalRepresentative(legalRep);

        // Projects
        List<Project> projects = businessDetailUtils.mapProjects(dto.getProjects(), entity);
        entity.setProjects(projects);

        // Facilities
        List<Facility> facilities = businessDetailUtils.mapFacilities(dto.getFacilities(), entity);
        entity.setFacilities(facilities);

        // Equipments
        List<Equipment> equipments = businessDetailUtils.mapEquipments(dto.getEquipments(), entity);
        entity.setEquipments(equipments);

        // Processes
        businessDetailUtils.syncProcesses(dto.getProcesses(), entity); // ĐÚNG

        // Attach UserAccount
        entity.getUserAccounts().add(userAccount);
        userAccount.setBusinessDetail(entity);

        if (isoFile != null && !isoFile.isEmpty()) {
            String filePath = uploadIsoCertFile(entity, isoFile);
            entity.setIsoCertificateFilePath(filePath);
        }

        // Save
        entity = businessDetailRepository.saveAndFlush(entity);
        userAccountRepository.flush();

        return businessDetailMapper.toResponse(entity);
    }


    @Override
    public BusinessDetailResponse updateBusinessDetail(
            UUID userAccountId,
            BusinessDetailDTO dto,
            MultipartFile isoFile) {

        BusinessDetail entity = businessDetailRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail", "userAccountId", userAccountId, BusinessDetailError.NOT_FOUND));

        businessDetailUtils.validateOperationDetails(dto.getOperationType(), dto.getSeasonalDescription());
        businessDetailUtils.validateUniqueFields(dto, entity.getBusinessDetailId());

        // =======================
        // 1. BASIC FIELD UPDATE
        // =======================
        businessDetailMapper.updateEntity(entity, dto);

        // =======================
        // 2. RELATIONS UPDATE
        // =======================

        // LegalRepresentative update
        LegalRepresentative legalRep = businessDetailUtils.fetchLegalRepresentative(dto.getLegalRepresentative());
        entity.setLegalRepresentative(legalRep);

        // Projects update
        List<Project> updatedProjects = businessDetailUtils.mapProjects(dto.getProjects(), entity);
        entity.setProjects(updatedProjects);

        // Facilities update
        List<Facility> updatedFacilities = businessDetailUtils.mapFacilities(dto.getFacilities(), entity);
        entity.setFacilities(updatedFacilities);

        // Equipments update
        List<Equipment> updatedEquipments = businessDetailUtils.mapEquipments(dto.getEquipments(), entity);
        entity.setEquipments(updatedEquipments);

        // Processes update
        businessDetailUtils.syncProcesses(dto.getProcesses(), entity); // ĐÚNG

        // =======================
        // 3. ISO FILE UPDATE
        // =======================
        if (isoFile != null && !isoFile.isEmpty()) {

            if (entity.getIsoCertificateFilePath() != null) {
                try {
                    fileStorageService.deleteFile(entity.getIsoCertificateFilePath());
                } catch (Exception e) {
                    log.warn("Failed to delete existing ISO cert file: {}", entity.getIsoCertificateFilePath(), e);
                }
            }

            String newFilePath = uploadIsoCertFile(entity, isoFile);
            entity.setIsoCertificateFilePath(newFilePath);
        }

        entity = businessDetailRepository.save(entity);
        return businessDetailMapper.toResponse(entity);
    }


    @Override
    public UUID findByBusinessDetailId(UUID userAccountId) {
        BusinessDetail entity = businessDetailRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        "userAccountId",
                        userAccountId,
                        BusinessDetailError.NOT_FOUND));
        return entity.getBusinessDetailId();
    }

    @Override
    public List<BusinessDetailResponse> findAll() {
        return businessDetailMapper.toResponseList(businessDetailRepository.findAll());
    }

    // @Override
    // public BusinessDetailWithHistoryConsumptionDTO
    // getBusinessDetailWithHistoryConsumption(UUID id) {
    // var entity = businessDetailRepository.findById(id)
    // .orElseThrow(() -> exceptionFactory.createNotFoundException(
    // "BusinessDetail",
    // "id",
    // id,
    // BusinessDetailError.BUSINESS_DETAIL_ID_NOT_FOUND
    // ));
    // return businessDetailWithHistoryConsumptionMapper.toDTO(entity);
    // }

    // ==================== FILE-SPECIFIC METHODS ====================

    /**
     * Upload ISO cert file for BusinessDetail (separate endpoint for file-only
     * upload)
     */
    @Transactional
    public BusinessDetailResponse uploadIsoCertificateFile(UUID userAccountId, MultipartFile file) {
        // Tìm BusinessDetail qua userAccountId để kiểm tra quyền
        BusinessDetail entity = businessDetailRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        "userAccountId",
                        userAccountId,
                        BusinessDetailError.NOT_FOUND));

        // Kiểm tra user có thuộc BusinessDetail không
        UserAccount userAccount = userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "UserAccount",
                        "userAccountId",
                        userAccountId,
                        UserError.NOT_FOUND));
        if (!entity.getUserAccounts().contains(userAccount)) {
            throw exceptionFactory.createCustomException(
                    "BusinessDetail",
                    List.of("userAccountId", "businessDetailId"),
                    List.of(userAccountId, entity.getBusinessDetailId()),
                    UserError.UNAUTHORIZED_ACCESS);
        }

        if (file.isEmpty()) {
            throw exceptionFactory.createValidationException(
                    "File", "cannot be empty", "", UserError.MISSING_REQUIRED_FIELD);
        }
        if (entity.getIsoCertificateFilePath() != null) {
            try {
                fileStorageService.deleteFile(entity.getIsoCertificateFilePath());
            } catch (Exception e) {
                log.warn("Failed to delete old ISO cert file: {}", entity.getIsoCertificateFilePath(), e);
            }
        }
        String filePath = uploadIsoCertFile(entity, file);
        entity.setIsoCertificateFilePath(filePath);
        entity = businessDetailRepository.save(entity);
        BusinessDetailResponse response = businessDetailMapper.toResponse(entity);
        log.info("Uploaded ISO cert file for BusinessDetail: {} for ID: {}", filePath, entity.getBusinessDetailId());
        return response;
    }

    /**
     * Delete ISO cert file for BusinessDetail (separate endpoint)
     */
    @Transactional
    public void deleteIsoCertificateFile(UUID userAccountId) {
        // Tìm BusinessDetail qua userAccountId để kiểm tra quyền
        BusinessDetail entity = businessDetailRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        "userAccountId",
                        userAccountId,
                        BusinessDetailError.NOT_FOUND));

        // Kiểm tra user có thuộc BusinessDetail không
        UserAccount userAccount = userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "UserAccount",
                        "userAccountId",
                        userAccountId,
                        UserError.NOT_FOUND));
        if (!entity.getUserAccounts().contains(userAccount)) {
            throw exceptionFactory.createCustomException(
                    "BusinessDetail",
                    List.of("userAccountId", "businessDetailId"),
                    List.of(userAccountId, entity.getBusinessDetailId()),
                    UserError.UNAUTHORIZED_ACCESS);
        }

        if (entity.getIsoCertificateFilePath() == null) {
            throw exceptionFactory.createNotFoundException(
                    "IsoCertFile",
                    "businessDetailId",
                    entity.getBusinessDetailId(),
                    BusinessDetailError.ISO_CERT_FILE_NOT_FOUND);
        }
        fileStorageService.deleteFile(entity.getIsoCertificateFilePath());
        entity.setIsoCertificateFilePath(null);
        entity = businessDetailRepository.save(entity);
        log.info("Deleted ISO cert file for BusinessDetail for ID: {}", entity.getBusinessDetailId());
    }

    /**
     * Check if ISO cert file exists for BusinessDetail
     */
    @Transactional(readOnly = true)
    public boolean hasIsoCertificateFile(UUID userAccountId) {
        BusinessDetail entity = businessDetailRepository.findByUserAccountId(userAccountId)
                .orElse(null);
        if (entity == null || entity.getIsoCertificateFilePath() == null) {
            return false;
        }
        return fileStorageService.fileExists(entity.getIsoCertificateFilePath());
    }

    // ==================== PRIVATE HELPERS ====================

    private String uploadIsoCertFile(BusinessDetail entity, MultipartFile file) {
        // Generate path: e.g., "business/{businessId}/iso-cert/{year}/{filename}"
        int year = LocalDateTime.now().getYear();
        String fileName = "iso-cert-14001-" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        String directory = "business/" + entity.getBusinessDetailId() + "/iso-cert/" + year;
        String filePath = fileStorageService.storeFile(directory, fileName, file);
        log.info("Uploaded ISO cert file to: {} for BusinessDetail: {}", filePath, entity.getBusinessDetailId());
        return filePath;
    }
}