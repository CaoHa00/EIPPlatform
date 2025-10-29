package com.EIPplatform.mapper.businessInformation;

import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import org.mapstruct.Mapper;
import com.EIPplatform.mapper.authentication.UserAccountMapper;

import java.util.List;
@Mapper(componentModel = "spring", uses = {
        UserAccountMapper.class,
        BusinessDetailHistoryConsumptionMapper.class
})
public interface  BusinessDetailMapper {
    BusinessDetailDTO toDTO(BusinessDetail entity);
    BusinessDetail toEntity(BusinessDetailDTO dto);
    List<BusinessDetailDTO> toDTOList(List<BusinessDetail> entities);
    List<BusinessDetail> toEntityList(List<BusinessDetailDTO> dtos);
}
