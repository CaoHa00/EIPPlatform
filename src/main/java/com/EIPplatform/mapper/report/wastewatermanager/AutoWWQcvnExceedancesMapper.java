package com.EIPplatform.mapper.report.wastewatermanager;

import com.EIPplatform.model.dto.report.wastewatermanager.autowwqcvnexceedances.AutoWWQcvnExceedancesCreateDTO;
import com.EIPplatform.model.dto.report.wastewatermanager.autowwqcvnexceedances.AutoWWQcvnExceedancesDTO;
import com.EIPplatform.model.entity.report.wastewatermanager.AutoWWQcvnExceedances;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AutoWWQcvnExceedancesMapper {
    AutoWWQcvnExceedancesMapper INSTANCE = Mappers.getMapper(AutoWWQcvnExceedancesMapper.class);

    @Mapping(target = "qcvnExceedId", ignore = true)
    @Mapping(target = "wasteWaterData", ignore = true)
    AutoWWQcvnExceedances toEntity(AutoWWQcvnExceedancesCreateDTO dto);

    @Mapping(source = "qcvnExceedId", target = "qcvnExceedId")
    AutoWWQcvnExceedancesDTO toDto(AutoWWQcvnExceedances entity);

    List<AutoWWQcvnExceedancesDTO> toDtoList(List<AutoWWQcvnExceedances> entities);

    default void updateFromDto(AutoWWQcvnExceedancesCreateDTO dto, @org.mapstruct.MappingTarget AutoWWQcvnExceedances entity) {
        entity.setParamName(dto.getParamName());
        entity.setExceedDaysCount(dto.getExceedDaysCount());
        entity.setQcvnLimitValue(dto.getQcvnLimitValue());
        entity.setExceedRatioPercent(dto.getExceedRatioPercent());
    }
}