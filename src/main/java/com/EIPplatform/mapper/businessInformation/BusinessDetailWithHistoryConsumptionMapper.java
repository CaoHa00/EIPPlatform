package com.EIPplatform.mapper.businessInformation;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.EIPplatform.model.dto.businessInformation.BusinessDetailWithHistoryConsumptionDTO;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
@Mapper(
    componentModel = "spring",
    uses = { BusinessDetailMapper.class, BusinessDetailHistoryConsumptionMapper.class }
)
public interface BusinessDetailWithHistoryConsumptionMapper {
    @Mapping(target = "businessDetailDTO", source = "entity")
    BusinessDetailWithHistoryConsumptionDTO toDTO(BusinessDetail entity);

    List<BusinessDetailWithHistoryConsumptionDTO> toDTOList(List<BusinessDetail> entities);
}
