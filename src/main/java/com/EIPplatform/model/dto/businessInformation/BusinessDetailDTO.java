package com.EIPplatform.model.dto.businessInformation;

import com.EIPplatform.model.dto.businessInformation.equipment.EquipmentResponseDto;
import com.EIPplatform.model.dto.businessInformation.facility.FacilityResponseDto;
import com.EIPplatform.model.dto.businessInformation.process.ProcessResponseDto;
import com.EIPplatform.model.dto.businessInformation.project.ProjectResponseDto;
import com.EIPplatform.model.enums.OperationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDetailDTO {

    @NotBlank(message = "FIELD_REQUIRED")
    String legalRepresentative;

    @NotBlank(message = "FIELD_REQUIRED")
    String phoneNumber;

    @NotBlank(message = "FIELD_REQUIRED")
    String address;

    @NotBlank(message = "FIELD_REQUIRED")
    String activityType;

    @NotBlank(message = "FIELD_REQUIRED")
    String scaleCapacity;
    String ISO_certificate_14001;

    @NotBlank(message = "FIELD_REQUIRED")
    String businessRegistrationNumber;

    @NotBlank(message = "FIELD_REQUIRED")
    String taxCode;

    @NotNull(message = "Operation type is required")
    OperationType operationType = OperationType.REGULAR;

    @Size(max = 500, message = "Description too long")
    String seasonalDescription;

    List<ProjectResponseDto> projects;

    List<FacilityResponseDto> facilities;

    List<EquipmentResponseDto> equipments;

    List<ProcessResponseDto> processes;
}
