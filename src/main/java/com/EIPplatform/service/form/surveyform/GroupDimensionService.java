package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ForbiddenError;
import com.EIPplatform.exception.errorCategories.FormError;
import com.EIPplatform.mapper.form.surveyform.GroupDimensionMapper;
import com.EIPplatform.model.dto.form.surveyform.question.CreateGroupDimensionDTO;
import com.EIPplatform.model.dto.form.surveyform.question.GroupDimensionDTO;
import com.EIPplatform.model.entity.form.surveyform.GroupDimension;
import com.EIPplatform.model.entity.form.surveyform.Dimension;
import com.EIPplatform.repository.form.surveyform.GroupDimensionRepository;
import com.EIPplatform.repository.form.surveyform.QuestionRepository;
import com.EIPplatform.repository.form.surveyform.DimensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupDimensionService implements GroupDimensionServiceInterface {

    private final GroupDimensionRepository groupDimensionRepository;
    private final DimensionRepository dimensionRepository;
    private final GroupDimensionMapper groupDimensionMapper;
    private final ExceptionFactory exceptionFactory;
    private final QuestionRepository questionRepository;
    private final SurveyAccessControlServiceInterface accessControlService;

    @Override
    public GroupDimensionDTO getById(UUID id) {
        GroupDimension gd = groupDimensionRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("GroupDimension", "id", id, FormError.GROUP_DIMENSION_NOT_FOUND));
        return groupDimensionMapper.toDTO(gd);
    }

    @Override
    public List<GroupDimensionDTO> getAll() {
        return groupDimensionMapper.toDTOList(groupDimensionRepository.findAll());
    }

    @Override
    @Transactional
    public List<GroupDimensionDTO> createGroupDimensionList(List<CreateGroupDimensionDTO> createGroupDimensionDTOs, UUID dimensionId, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        Dimension parentDimension = dimensionRepository.findById(dimensionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Dimension", "id", dimensionId, FormError.DIMENSION_NOT_FOUND));

        List<GroupDimension> groupDimensions = createGroupDimensionDTOs.stream()
                .map(dto -> buildGroupDimensionEntity(dto, parentDimension))
                .collect(Collectors.toList());

        List<GroupDimension> savedGroupDimensions = groupDimensionRepository.saveAll(groupDimensions);

        return groupDimensionMapper.toDTOList(savedGroupDimensions);
    }

    @Override
    @Transactional
    public void deleteGroupDimension(UUID id, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        GroupDimension groupDimension = groupDimensionRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("GroupDimension", "id", id, FormError.GROUP_DIMENSION_NOT_FOUND));

        //check if any existing Question entity uses this GroupDimension
        if (questionRepository.existsByGroupDimension_Id(id)) {
            throw exceptionFactory.createCustomException("GroupDimension",
                    Collections.singletonList("id"),
                    Collections.singletonList(id),
                    FormError.GROUP_DIMENSION_IN_USE);
        }

        groupDimensionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public GroupDimensionDTO editGroupDimension(UUID id, CreateGroupDimensionDTO dto, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        GroupDimension groupDimension = groupDimensionRepository.findById(id).orElseThrow(() ->
                exceptionFactory.createNotFoundException("GroupDimension", "id", id, FormError.GROUP_DIMENSION_NOT_FOUND));

        groupDimension.setName(dto.getName());
        groupDimension.setCode(dto.getCode());

        groupDimensionRepository.save(groupDimension);

        return groupDimensionMapper.toDTO(groupDimension);
    }

    public GroupDimension buildGroupDimensionEntity(CreateGroupDimensionDTO dto, Dimension parent) {
        GroupDimension groupDimension = new GroupDimension();
        groupDimension.setName(dto.getName());
        groupDimension.setDimension(parent);
        groupDimension.setCode(dto.getCode());
        return groupDimension;
    }
}
