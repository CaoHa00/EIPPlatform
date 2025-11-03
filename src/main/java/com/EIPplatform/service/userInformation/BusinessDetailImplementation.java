package com.EIPplatform.service.userInformation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.UserError;
import com.EIPplatform.mapper.businessInformation.BusinessDetailMapper;
import com.EIPplatform.mapper.businessInformation.BusinessDetailWithHistoryConsumptionMapper;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailResponse;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailWithHistoryConsumptionDTO;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.repository.authentication.UserAccountRepository;
import com.EIPplatform.repository.user.BusinessDetailRepository;
import com.EIPplatform.service.fileStorage.FileStorageService;
import com.EIPplatform.util.BusinessDetailUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
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
    BusinessDetailWithHistoryConsumptionMapper businessDetailWithHistoryConsumptionMapper;
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
                        UserError.NOT_FOUND
                ));
    }

    @Override
    public void deleteByBusinessDetailId(UUID businessDetailId) {
        BusinessDetail entity = businessDetailRepository.findById(businessDetailId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        "businessDetailId",
                        businessDetailId,
                        UserError.NOT_FOUND
                ));

        // Delete ISO file if exists
        if (entity.getIsoCertificateFilePath() != null) {
            try {
                fileStorageService.deleteFile(entity.getIsoCertificateFilePath());
                log.info("Deleted ISO cert file for BusinessDetail: {}", entity.getIsoCertificateFilePath());
            } catch (Exception e) {
                log.warn("Failed to delete ISO cert file: {}", entity.getIsoCertificateFilePath(), e);
            }
        }

        businessDetailRepository.deleteById(businessDetailId);
    }

    @Override
    public BusinessDetailResponse createBusinessDetail(UUID userAccountId, BusinessDetailDTO dto, MultipartFile isoFile) {
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

        // Handle ISO file upload if provided
        if (isoFile != null && !isoFile.isEmpty()) {
            String filePath = uploadIsoCertFile(entity, isoFile);
            entity.setIsoCertificateFilePath(filePath);
        }

        entity = businessDetailRepository.saveAndFlush(entity);

        if (entity.getBusinessDetailId() == null) {
            throw exceptionFactory.createCustomException(
                    "BusinessDetail",
                    List.of("operation", "companyName"),
                    List.of("save", dto.getCompanyName()),
                    UserError.ID_GENERATION_FAILED
            );
        }

        userAccountRepository.flush();

        BusinessDetailResponse response = businessDetailMapper.toResponse(entity);

        log.info("Created BusinessDetail - ID: {}, Company: {}, TaxCode: {}",
                entity.getBusinessDetailId(), entity.getCompanyName(), entity.getTaxCode());

        return response;
    }

    @Override
    public BusinessDetailResponse updateBusinessDetail(UUID id, BusinessDetailDTO dto, MultipartFile isoFile) {
        BusinessDetail entity = businessDetailRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        "id",
                        id,
                        UserError.NOT_FOUND
                ));

        businessDetailUtils.validateOperationDetails(dto.getOperationType(), dto.getSeasonalDescription());

        businessDetailUtils.validateUniqueFields(dto, id);

        entity.setCompanyName(dto.getCompanyName());
        entity.setLegalRepresentative(dto.getLegalPresentative());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setLocation(dto.getLocation());
        entity.setIndustrySector(dto.getIndustrySector());
        entity.setScaleCapacity(dto.getScaleCapacity());
        entity.setISO_certificate_14001(dto.getISO_certificate_14001());
        entity.setBusinessRegistrationNumber(dto.getBusinessRegistrationNumber());
        entity.setTaxCode(dto.getTaxCode());
        entity.setOperationType(dto.getOperationType());
        entity.setSeasonalDescription(dto.getSeasonalDescription());

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
                id, entity.getCompanyName(), entity.getTaxCode());

        return response;
    }

    @Override
    public List<BusinessDetailResponse> findAll() {
        return businessDetailMapper.toResponseList(businessDetailRepository.findAll());
    }

    @Override
    public BusinessDetailWithHistoryConsumptionDTO getBusinessDetailWithHistoryConsumption(UUID id) {
        var entity = businessDetailRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        "id",
                        id,
                        UserError.NOT_FOUND
                ));
        return businessDetailWithHistoryConsumptionMapper.toDTO(entity);
    }

    // ==================== FILE-SPECIFIC METHODS ====================

    /**
     * Upload ISO cert file for BusinessDetail (separate endpoint for file-only upload)
     */
    @Transactional
    public BusinessDetailResponse uploadIsoCertificateFile(UUID businessDetailId, MultipartFile file) {
        BusinessDetail entity = businessDetailRepository.findById(businessDetailId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        "businessDetailId",
                        businessDetailId,
                        UserError.NOT_FOUND
                ));

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

        log.info("Uploaded ISO cert file for BusinessDetail: {} for ID: {}", filePath, businessDetailId);
        return response;
    }

    /**
     * Delete ISO cert file for BusinessDetail (separate endpoint)
     */
    @Transactional
    public void deleteIsoCertificateFile(UUID businessDetailId) {
        BusinessDetail entity = businessDetailRepository.findById(businessDetailId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        "businessDetailId",
                        businessDetailId,
                        UserError.NOT_FOUND
                ));

        if (entity.getIsoCertificateFilePath() == null) {
            throw exceptionFactory.createNotFoundException(
                    "IsoCertFile", "businessDetail", businessDetailId.toString(), UserError.NOT_FOUND
            );
        }

        fileStorageService.deleteFile(entity.getIsoCertificateFilePath());
        entity.setIsoCertificateFilePath(null);
        entity = businessDetailRepository.save(entity);

        BusinessDetailResponse response = businessDetailMapper.toResponse(entity);

        log.info("Deleted ISO cert file for BusinessDetail for ID: {}", businessDetailId);
    }

    /**
     * Check if ISO cert file exists for BusinessDetail
     */
    @Transactional(readOnly = true)
    public boolean hasIsoCertificateFile(UUID businessDetailId) {
        BusinessDetail entity = businessDetailRepository.findById(businessDetailId)
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