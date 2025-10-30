package com.EIPplatform.util;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ForbiddenError;
import com.EIPplatform.exception.errorCategories.PermitError;
import com.EIPplatform.exception.errorCategories.UserError;
import com.EIPplatform.exception.errorCategories.ValidationError;
import com.EIPplatform.model.dto.fileStorage.FileStorageRequest;
import com.EIPplatform.model.entity.permitshistory.EnvComponentPermit;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.repository.authentication.UserAccountRepository;
import com.EIPplatform.repository.user.BusinessDetailRepository;
import com.EIPplatform.repository.permitshistory.EnvComponentPermitRepository;
import com.EIPplatform.service.fileStorage.FileStorageService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.UUID;

public class PermitUtils {

    static FileStorageService fileStorageService;

    public static void validateUserExists(UserAccountRepository userAccountRepository, UUID userAccountId, ExceptionFactory exceptionFactory) {
        userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "UserAccount",
                        "userAccountId",
                        userAccountId,
                        UserError.NOT_FOUND
                ));
    }

    public static BusinessDetail getBusinessDetailByUserAccountId(BusinessDetailRepository businessDetailRepository, UUID userAccountId, ExceptionFactory exceptionFactory) {
        return businessDetailRepository.findByUserAccounts_UserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail", "userAccountId", userAccountId, UserError.NOT_FOUND
                ));
    }

    public static EnvComponentPermit getComponentPermitAndValidateOwnership(EnvComponentPermitRepository componentPermitRepository, Long permitId, UUID userAccountId, BusinessDetailRepository businessDetailRepository, ExceptionFactory exceptionFactory) {
        EnvComponentPermit permit = componentPermitRepository.findById(permitId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ComponentPermit", "permitId", permitId, PermitError.NOT_FOUND));

        BusinessDetail businessDetail = permit.getBusinessDetail();

        boolean isOwner = businessDetail.getUserAccounts().stream()
                .anyMatch(account -> account.getUserAccountId().equals(userAccountId));

        if (!isOwner) {
            throw exceptionFactory.createCustomException(
                    "Permit",
                    Arrays.asList("accessReason"),
                    Arrays.asList("You do not have access to this permit"),
                    ForbiddenError.FORBIDDEN
            );
        }

        return permit;
    }

    public static String uploadPermitFile(BusinessDetail businessDetail, MultipartFile file, String subFolder, int year, FileStorageService fileStorageService, ExceptionFactory exceptionFactory) {
        validatePermitFile(file, exceptionFactory);

        FileStorageRequest storageRequest = FileStorageRequest.builder()
                .businessName(businessDetail.getCompanyName())
                .sector("permits/" + subFolder)
                .year(year)
                .build();

        return fileStorageService.uploadFile(file, storageRequest);
    }

    public static void validatePermitFile(MultipartFile file, ExceptionFactory exceptionFactory) {
        if (file.isEmpty()) {
            throw exceptionFactory.createValidationException(
                    "File", "empty", true, ValidationError.EMPTY_FILE
            );
        }

        String contentType = file.getContentType();
        java.util.List<String> allowedTypes = Arrays.asList("application/pdf", "image/jpeg", "image/png");

        if (!allowedTypes.contains(contentType)) {
            throw exceptionFactory.createValidationException(
                    "File", "contentType", contentType, ValidationError.INVALID_TYPE
            );
        }

        long maxSize = 10 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw exceptionFactory.createValidationException(
                    "File", "size", file.getSize(), ValidationError.TOO_LARGE
            );
        }

        String filename = file.getOriginalFilename();
        if (filename != null) {
            String extension = fileStorageService.getFileExtension(filename).toLowerCase();
            java.util.List<String> allowedExtensions = Arrays.asList(".pdf", ".jpg", ".jpeg", ".png");

            if (!allowedExtensions.contains(extension)) {
                throw exceptionFactory.createValidationException(
                        "File", "extension", extension, ValidationError.INVALID_TYPE
                );
            }
        }
    }

    public static void checkDuplicateComponentPermitNumber(EnvComponentPermitRepository componentPermitRepository, UUID businessDetailId, String permitNumber, ExceptionFactory exceptionFactory) {
        if (permitNumber != null && !permitNumber.trim().isEmpty()) {
            if (componentPermitRepository.existsByBusinessDetail_BusinessDetailIdAndPermitNumber(
                    businessDetailId, permitNumber)) {
                throw exceptionFactory.createValidationException(
                        "PermitNumber", "duplicate", permitNumber, ValidationError.DUPLICATE_VALUE
                );
            }
        }
    }
}