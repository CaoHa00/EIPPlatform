package com.EIPplatform.mapper.report.reportB04.part1;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.EIPplatform.model.dto.report.reportB04.part1.ThirdPartyImplementerDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ThirdPartyImplementerCreationRequest;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ThirdPartyImplementerUpdateRequest;
import com.EIPplatform.model.dto.report.reportB04.part1.response.ThirdPartyImplementerResponse;
import com.EIPplatform.model.entity.report.reportB04.part01.ThirdPartyImplementer;

@Mapper(componentModel = "spring")
public interface ThirdPartyImplementerMapper {

    // CreateRequest → Entity
    ThirdPartyImplementer toEntityFromCreate(ThirdPartyImplementerCreationRequest request);

    // UpdateRequest → Entity (ignore null fields)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdate(ThirdPartyImplementerUpdateRequest request,
            @MappingTarget ThirdPartyImplementer entity);

    // Entity → Response
    ThirdPartyImplementerResponse toResponse(ThirdPartyImplementer entity);

    List<ThirdPartyImplementerResponse> toResponseList(List<ThirdPartyImplementer> entities);

    @Named("toThirdPartyDTO")
    default ThirdPartyImplementerDTO map(ThirdPartyImplementer entity) {
        if (entity == null)
            return null;
        return ThirdPartyImplementerDTO.builder()
                .orgName(entity.getOrgName())
                .orgDocType(entity.getOrgDocType())
                .orgDocNumber(entity.getOrgDocNumber())
                .orgDocIssuer(entity.getOrgDocIssuer())
                .orgDocIssueDate(entity.getOrgDocIssueDate())
                .orgDocAmendDate(entity.getOrgDocAmendDate())
                .build();
    }
}