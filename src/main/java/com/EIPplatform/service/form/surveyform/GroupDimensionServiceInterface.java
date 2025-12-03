package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.question.CreateGroupDimensionDTO;
import com.EIPplatform.model.dto.form.surveyform.question.GroupDimensionDTO;
import com.EIPplatform.model.entity.form.surveyform.GroupDimension;
import com.EIPplatform.model.entity.form.surveyform.Dimension;

import java.util.List;
import java.util.UUID;

public interface GroupDimensionServiceInterface {
    GroupDimension buildGroupDimensionEntity(CreateGroupDimensionDTO dto, Dimension parent);
    GroupDimensionDTO getById(UUID id);
    List<GroupDimensionDTO> getAll();
    List<GroupDimensionDTO> createGroupDimensionList(List<CreateGroupDimensionDTO> list, UUID dimensionId, UUID userAccountId);
    void deleteGroupDimension(UUID id, UUID userAccountId);
    GroupDimensionDTO editGroupDimension(UUID id, CreateGroupDimensionDTO dto, UUID userAccountId);
}
