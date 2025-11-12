package com.EIPplatform.model.dto.businessInformation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessDetailResponse {

    UUID businessDetailId;
    String facilityName;
    String legalRepresentative;
    String phoneNumber;
    String address;
    String activityType;
    String scaleCapacity;
    String ISO_certificate_14001;
    String isoCertificateFilePath;
    String businessRegistrationNumber;
    String taxCode;
    OperationType operationType;
    String seasonalDescription;
    String operationFrequency;

    List<String> userAccounts;

    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime updatedAt;
    String updatedBy;
}