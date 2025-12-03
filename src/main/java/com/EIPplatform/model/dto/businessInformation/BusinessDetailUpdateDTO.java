package com.EIPplatform.model.dto.businessInformation;

import com.EIPplatform.model.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDetailUpdateDTO {
    String facilityName;

    String legalRepresentative;

    String phoneNumber;

    String address;

    String activityType;

    String scaleCapacity;
    String ISO_certificate_14001;

    String businessRegistrationNumber;

    String taxCode;

    OperationType operationType = OperationType.REGULAR;

    String seasonalDescription;
}
