
package com.EIPplatform.utils;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.BusinessDetailError;
import com.EIPplatform.mapper.businessInformation.EquipmentMapper;
import com.EIPplatform.mapper.businessInformation.FacilityMapper;
import com.EIPplatform.mapper.businessInformation.ProcessMapper;
import com.EIPplatform.mapper.businessInformation.ProjectMapper;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.businessInformation.equipment.EquipmentResponseDto;
import com.EIPplatform.model.dto.businessInformation.facility.FacilityResponseDto;
import com.EIPplatform.model.dto.businessInformation.process.ProcessResponseDto;
import com.EIPplatform.model.dto.businessInformation.project.ProjectResponseDto;
import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.businessInformation.Equipment;
import com.EIPplatform.model.entity.businessInformation.Facility;
import com.EIPplatform.model.entity.businessInformation.Process;
import com.EIPplatform.model.entity.businessInformation.Project;
import com.EIPplatform.model.entity.businessInformation.legalRepresentative.LegalRepresentative;
import com.EIPplatform.model.enums.OperationType;
import com.EIPplatform.repository.businessInformation.BusinessDetailRepository;
import com.EIPplatform.repository.user.LegalRepresentativeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BusinessDetailUtils {

    private final BusinessDetailRepository businessDetailRepository;
    private final ExceptionFactory exceptionFactory;
    private final LegalRepresentativeRepository legalRepRepo;
    private final ProjectMapper projectMapper;
    private final FacilityMapper facilityMapper;
    private final EquipmentMapper equipmentMapper;
    private final ProcessMapper processMapper;

    public void validateOperationDetails(OperationType operationType, String seasonalDescription) {
        if (OperationType.SEASONAL.equals(operationType)) {
            if (seasonalDescription == null || seasonalDescription.trim().isEmpty()) {
                throw new IllegalArgumentException("Seasonal must provide description of operating hours");
            }
        }
    }

    public void validateUniqueFields(BusinessDetailDTO dto, UUID currentId) {
        // Validate taxCode
        Optional<BusinessDetail> existingTax = businessDetailRepository.findByTaxCode(dto.getTaxCode());
        if (existingTax.isPresent() && (currentId == null || !existingTax.get().getBusinessDetailId().equals(currentId))) {
            throw exceptionFactory.createAlreadyExistsException(
                    "BusinessDetail", "taxCode", dto.getTaxCode(), BusinessDetailError.TAX_CODE_DUPLICATE
            );
        }

        // Validate businessRegistrationNumber
        Optional<BusinessDetail> existingRegNum = businessDetailRepository.findByBusinessRegistrationNumber(dto.getBusinessRegistrationNumber());
        if (existingRegNum.isPresent() && (currentId == null || !existingRegNum.get().getBusinessDetailId().equals(currentId))) {
            throw exceptionFactory.createAlreadyExistsException(
                    "BusinessDetail", "businessRegistrationNumber", dto.getBusinessRegistrationNumber(), BusinessDetailError.BUSINESS_REGISTRATION_NUMBER_DUPLICATE
            );
        }

        // Validate phoneNumber
        Optional<BusinessDetail> existingPhone = businessDetailRepository.findByPhoneNumber(dto.getPhoneNumber());
        if (existingPhone.isPresent() && (currentId == null || !existingPhone.get().getBusinessDetailId().equals(currentId))) {
            throw exceptionFactory.createAlreadyExistsException(
                    "BusinessDetail", "phoneNumber", dto.getPhoneNumber(), BusinessDetailError.PHONE_NUMBER_DUPLICATE
            );
        }
    }

    public LegalRepresentative fetchLegalRepresentative(String id) {
        return legalRepRepo.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("LegalRepresentative not found"));
    }

    public List<Project> mapProjects(List<ProjectResponseDto> dtos, BusinessDetail parent) {
        if (dtos == null) return new ArrayList<>();
        List<Project> list = projectMapper.toEntityList(dtos);
        list.forEach(p -> p.setBusinessDetail(parent));
        return list;
    }

    public List<Facility> mapFacilities(List<FacilityResponseDto> dtos, BusinessDetail parent) {
        if (dtos == null) return new ArrayList<>();
        List<Facility> list = facilityMapper.toEntityList(dtos);
        list.forEach(f -> f.setBusinessDetail(parent));
        return list;
    }

    public List<Equipment> mapEquipments(List<EquipmentResponseDto> dtos, BusinessDetail parent) {
        if (dtos == null) return new ArrayList<>();
        List<Equipment> list = equipmentMapper.toEntityList(dtos);
        list.forEach(e -> e.setBusinessDetail(parent));
        return list;
    }

    public void syncProcesses(List<ProcessResponseDto> dtos, BusinessDetail entity) {
        if (entity == null) return;

        List<Process> current = entity.getProcesses();

        if (dtos == null || dtos.isEmpty()) {
            current.clear();
            return;
        }

        List<Process> updatedList = dtos.stream()
                .map(dto -> {
                    Process process;

                    if (dto.getProcessId() != null) {
                        process = current.stream()
                                .filter(p -> dto.getProcessId().equals(p.getProcessId()))
                                .findFirst()
                                .orElseGet(() -> {
                                    // Không tìm thấy → tạo mới
                                    Process newProcess = processMapper.toEntity(dto);
                                    newProcess.setBusinessDetail(entity);
                                    return newProcess;
                                });
                    } else {
                        process = processMapper.toEntity(dto);
                        process.setBusinessDetail(entity);
                    }
                    processMapper.updateFromDto(process, dto);
                    process.setBusinessDetail(entity);
                    return process;
                })
                .toList();
        current.removeIf(existing ->
                updatedList.stream().noneMatch(newOne ->
                        Objects.equals(existing.getProcessId(), newOne.getProcessId()))
        );
        for (Process updated : updatedList) {
            if (updated.getProcessId() == null ||
                    current.stream().noneMatch(p -> p.getProcessId().equals(updated.getProcessId()))) {
                current.add(updated);
            }
        }
    }
}