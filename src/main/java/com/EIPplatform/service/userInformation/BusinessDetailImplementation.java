package com.EIPplatform.service.userInformation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.BusinessDetailError;
import com.EIPplatform.exception.errorCategories.UserError;
import com.EIPplatform.mapper.businessInformation.BusinessDetailMapper;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailResponse;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailUpdateDTO;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.repository.authentication.UserAccountRepository;
import com.EIPplatform.repository.user.BusinessDetailRepository;
import com.EIPplatform.service.fileStorage.FileStorageService;
import com.EIPplatform.util.BusinessDetailUtils;

import com.EIPplatform.util.StringNormalizerUtil;
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
                        BusinessDetailError.NOT_FOUND
                ));
    }

    @Override
    public void deleteByUserAccountId(UUID userAccountId) {
        BusinessDetail entity = businessDetailRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        "userAccountId",
                        userAccountId,
                        BusinessDetailError.NOT_FOUND
                ));
        UserAccount userAccount = userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "UserAccount",
                        "userAccountId",
                        userAccountId,
                        UserError.NOT_FOUND
                ));
        if (!entity.getUserAccounts().contains(userAccount)) {
            throw exceptionFactory.createCustomException(
                    "BusinessDetail",
                    List.of("userAccountId", "businessDetailId"),
                    List.of(userAccountId, entity.getBusinessDetailId()),
                    UserError.UNAUTHORIZED_ACCESS
            );
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
    public BusinessDetailResponse createBusinessDetail(UUID userAccountId, BusinessDetailDTO dto, MultipartFile isoFile) {
        dto = StringNormalizerUtil.normalizeRequest(dto);
        businessDetailUtils.validateOperationDetails(dto.getOperationType(), dto.getSeasonalDescription());
        businessDetailUtils.validateUniqueFields(dto, null);
        UserAccount userAccount = userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "UserAccount",
                        "userAccountId",
                        userAccountId,
                        UserError.NOT_FOUND
                ));
        BusinessDetail entity = businessDetailMapper.toEntity(dto);
        entity.getUserAccounts().add(userAccount);
        userAccount.setBusinessDetail(entity);
        if (isoFile != null && !isoFile.isEmpty()) {
            String filePath = uploadIsoCertFile(entity, isoFile);
            entity.setIsoCertificateFilePath(filePath);
        }
        entity = businessDetailRepository.saveAndFlush(entity);
        if (entity.getBusinessDetailId() == null) {
            throw exceptionFactory.createCustomException(
                    "BusinessDetail",
                    List.of("operation", "companyName"),
                    List.of("save", dto.getFacilityName()),
                    UserError.ID_GENERATION_FAILED
            );
        }
        userAccountRepository.flush();
        BusinessDetailResponse response = businessDetailMapper.toResponse(entity);
        log.info("Created BusinessDetail - ID: {}, Company: {}, TaxCode: {}",
                entity.getBusinessDetailId(), entity.getFacilityName(), entity.getTaxCode());
        return response;
    }

    @Override
    public BusinessDetailResponse updateBusinessDetail(UUID userAccountId, BusinessDetailUpdateDTO dto, MultipartFile isoFile) {
        dto = StringNormalizerUtil.normalizeRequest(dto);
        BusinessDetail entity = businessDetailRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        "userAccountId",
                        userAccountId,
                        BusinessDetailError.NOT_FOUND
                ));

        businessDetailUtils.validateOperationDetails(dto.getOperationType(), dto.getSeasonalDescription());
        businessDetailUtils.validateUniqueFields(dto, entity.getBusinessDetailId());

        if (dto.getFacilityName() != null) {
            entity.setFacilityName(dto.getFacilityName().trim());
        }
        if (dto.getLegalRepresentative() != null) {
            entity.setLegalRepresentative(dto.getLegalRepresentative().trim());
        }
        if (dto.getPhoneNumber() != null) {
            entity.setPhoneNumber(dto.getPhoneNumber().trim());
        }
        if (dto.getAddress() != null) {
            entity.setAddress(dto.getAddress().trim());
        }
        if (dto.getActivityType() != null) {
            entity.setActivityType(dto.getActivityType().trim());
        }
        if (dto.getScaleCapacity() != null) {
            entity.setScaleCapacity(dto.getScaleCapacity().trim());
        }
        if (dto.getISO_certificate_14001() != null) {
            entity.setISO_certificate_14001(dto.getISO_certificate_14001().trim());
        }
        if (dto.getBusinessRegistrationNumber() != null) {
            entity.setBusinessRegistrationNumber(dto.getBusinessRegistrationNumber().trim());
        }
        if (dto.getTaxCode() != null) {
            entity.setTaxCode(dto.getTaxCode().trim());
        }
        if (dto.getOperationType() != null) {
            entity.setOperationType(dto.getOperationType());
        }
        if (dto.getSeasonalDescription() != null) {
            entity.setSeasonalDescription(dto.getSeasonalDescription().trim());
        }

        // Handle ISO file update if provided
        if (isoFile != null && !isoFile.isEmpty()) {
            if (entity.getIsoCertificateFilePath() != null) {
                try {
                    fileStorageService.deleteFile(entity.getIsoCertificateFilePath());
                } catch (Exception e) {
                    log.warn("Failed to delete old ISO cert file: {}", entity.getIsoCertificateFilePath(), e);
                }
            }
            String filePath = uploadIsoCertFile(entity, isoFile);
            entity.setIsoCertificateFilePath(filePath);
        }

        entity = businessDetailRepository.save(entity);
        BusinessDetailResponse response = businessDetailMapper.toResponse(entity);
        log.info("Updated BusinessDetail - ID: {}, Company: {}, TaxCode: {}",
                entity.getBusinessDetailId(), entity.getFacilityName(), entity.getTaxCode());
        return response;
    }

    @Override
    public UUID findByBusinessDetailId(UUID userAccountId) {
        BusinessDetail entity = businessDetailRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        "userAccountId",
                        userAccountId,
                        BusinessDetailError.NOT_FOUND
                ));
        return entity.getBusinessDetailId();
    }

    @Override
    public List<BusinessDetailResponse> findAll() {
        return businessDetailMapper.toResponseList(businessDetailRepository.findAll());
    }

//    @Override
//    public BusinessDetailWithHistoryConsumptionDTO getBusinessDetailWithHistoryConsumption(UUID id) {
//        var entity = businessDetailRepository.findById(id)
//                .orElseThrow(() -> exceptionFactory.createNotFoundException(
//                        "BusinessDetail",
//                        "id",
//                        id,
//                        BusinessDetailError.BUSINESS_DETAIL_ID_NOT_FOUND
//                ));
//        return businessDetailWithHistoryConsumptionMapper.toDTO(entity);
//    }

    // ==================== FILE-SPECIFIC METHODS ====================

    /**
     * Upload ISO cert file for BusinessDetail (separate endpoint for file-only upload)
     */
    @Transactional
    public BusinessDetailResponse uploadIsoCertificateFile(UUID userAccountId, MultipartFile file) {
        // Tìm BusinessDetail qua userAccountId để kiểm tra quyền
        BusinessDetail entity = businessDetailRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        "userAccountId",
                        userAccountId,
                        BusinessDetailError.NOT_FOUND
                ));

        // Kiểm tra user có thuộc BusinessDetail không
        UserAccount userAccount = userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "UserAccount",
                        "userAccountId",
                        userAccountId,
                        UserError.NOT_FOUND
                ));
        if (!entity.getUserAccounts().contains(userAccount)) {
            throw exceptionFactory.createCustomException(
                    "BusinessDetail",
                    List.of("userAccountId", "businessDetailId"),
                    List.of(userAccountId, entity.getBusinessDetailId()),
                    UserError.UNAUTHORIZED_ACCESS
            );
        }

        if (file.isEmpty()) {
            throw exceptionFactory.createValidationException(
                    "File", "cannot be empty", "", UserError.MISSING_REQUIRED_FIELD
            );
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
                        BusinessDetailError.NOT_FOUND
                ));

        // Kiểm tra user có thuộc BusinessDetail không
        UserAccount userAccount = userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "UserAccount",
                        "userAccountId",
                        userAccountId,
                        UserError.NOT_FOUND
                ));
        if (!entity.getUserAccounts().contains(userAccount)) {
            throw exceptionFactory.createCustomException(
                    "BusinessDetail",
                    List.of("userAccountId", "businessDetailId"),
                    List.of(userAccountId, entity.getBusinessDetailId()),
                    UserError.UNAUTHORIZED_ACCESS
            );
        }

        if (entity.getIsoCertificateFilePath() == null) {
            throw exceptionFactory.createNotFoundException(
                    "IsoCertFile",
                    "businessDetailId",
                    entity.getBusinessDetailId(),
                    BusinessDetailError.ISO_CERT_FILE_NOT_FOUND
            );
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