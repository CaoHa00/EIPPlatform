package com.EIPplatform.mapper.report.report06.part02;

import com.EIPplatform.model.dto.report.report06.part02.operationalActivityData.OperationalActivityDataCreateDTO;
import com.EIPplatform.model.dto.report.report06.part02.operationalActivityData.OperationalActivityDataDTO;
import com.EIPplatform.model.dto.report.report06.part02.operationalActivityData.OperationalActivityDataUpdateDTO;
import com.EIPplatform.model.entity.businessInformation.Equipment;
import com.EIPplatform.model.entity.businessInformation.Facility;
import com.EIPplatform.model.entity.businessInformation.Process;
import com.EIPplatform.model.entity.report.report06.part02.OperationalActivityData;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = {EmissionSourceMapper.class, CarbonSinkProjectMapper.class, LimitationMapper.class})
public interface OperationalActivityDataMapper {

    OperationalActivityDataMapper INSTANCE = Mappers.getMapper(OperationalActivityDataMapper.class);

    @Mapping(target = "report06", ignore = true)
    @Mapping(target = "scaleCapacity", ignore = true)
    @Mapping(target = "facilities", ignore = true)
    @Mapping(target = "processes", ignore = true)
    @Mapping(target = "equipments", ignore = true)
    @Mapping(target = "emissionSources", source = "emissionSources")
    @Mapping(target = "carbonSinkProjects", source = "carbonSinkProjects")
    @Mapping(target = "limitations", source = "limitations")
    OperationalActivityData toEntity(OperationalActivityDataCreateDTO createDTO);

    OperationalActivityDataDTO toDTO(OperationalActivityData entity);

    // OperationalActivityDataMapper
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "report06", ignore = true)
    @Mapping(target = "scaleCapacity", ignore = true)
    @Mapping(target = "facilities", ignore = true)
    @Mapping(target = "processes", ignore = true)
    @Mapping(target = "equipments", ignore = true)
    @Mapping(target = "emissionSources", ignore = true)
    @Mapping(target = "carbonSinkProjects", ignore = true)
    @Mapping(target = "limitations", ignore = true)
    void updateEntityFromDto(OperationalActivityDataDTO dto,
                             @MappingTarget OperationalActivityData entity);

    default List<UUID> mapFacilityIds(OperationalActivityData entity) {
        if (entity.getFacilities() == null) {
            return new ArrayList<>();
        }
        return entity.getFacilities().stream()
                .map(Facility::getFacilityId)
                .collect(Collectors.toList());
    }

    default List<UUID> mapProcessIds(OperationalActivityData entity) {
        if (entity.getProcesses() == null) {
            return new ArrayList<>();
        }
        return entity.getProcesses().stream()
                .map(Process::getProcessId)
                .collect(Collectors.toList());
    }

    default List<UUID> mapEquipmentIds(OperationalActivityData entity) {
        if (entity.getEquipments() == null) {
            return new ArrayList<>();
        }
        return entity.getEquipments().stream()
                .map(Equipment::getEquipmentId)
                .collect(Collectors.toList());
    }
}