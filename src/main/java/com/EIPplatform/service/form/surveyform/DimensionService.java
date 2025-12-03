package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ForbiddenError;
import com.EIPplatform.exception.errorCategories.FormError;
import com.EIPplatform.mapper.form.surveyform.DimensionMapper;
import com.EIPplatform.model.dto.form.surveyform.survey.CreateDimensionDTO;
import com.EIPplatform.model.dto.form.surveyform.survey.DimensionDTO;
import com.EIPplatform.model.entity.form.surveyform.GroupDimension;
import com.EIPplatform.model.entity.form.surveyform.Dimension;
import com.EIPplatform.model.entity.form.surveyform.SurveyForm;
import com.EIPplatform.repository.form.surveyform.QuestionRepository;
import com.EIPplatform.repository.form.surveyform.DimensionRepository;
import com.EIPplatform.repository.form.surveyform.SurveyFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DimensionService implements DimensionServiceInterface {

    private final DimensionRepository dimensionRepository;
    private final GroupDimensionService groupDimensionService;
    private final DimensionMapper dimensionMapper;
    private final ExceptionFactory exceptionFactory;
    private final SurveyFormRepository surveyFormRepository;
    private final QuestionRepository questionRepository;
    private final SurveyAccessControlServiceInterface accessControlService;


    @Override
    public DimensionDTO getById(UUID id){
        Dimension dimension = dimensionRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Dimension", "id", id, FormError.DIMENSION_NOT_FOUND));

        return dimensionMapper.toDTO(dimension);
    }

    @Override
    public List<DimensionDTO> getAll(){
        return dimensionMapper.toDTOList(dimensionRepository.findAll());
    }

    @Override
    public List<DimensionDTO> getAllBySurveyFormId(UUID surveyFormId) {
        List<Dimension> dimensions = dimensionRepository.findBySurveyFormId(surveyFormId);


        return dimensionMapper.toDTOList(dimensions);
    }

    @Override
    @Transactional
    public List<DimensionDTO> createDimensionList(List<CreateDimensionDTO> createDimensionDTOs, UUID surveyFormId, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        SurveyForm surveyForm = surveyFormRepository.findById(surveyFormId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("SurveyForm", "id", surveyFormId, FormError.SURVEY_FORM_NOT_FOUND));

        List<Dimension> dimensions = createDimensionDTOs.stream()
                .map(dto -> buildDimensionEntity(dto, surveyForm))
                .collect(Collectors.toList());

        List<Dimension> savedDimensions = dimensionRepository.saveAll(dimensions);

        return dimensionMapper.toDTOList(savedDimensions);
    }

    /**
     * Delete a Dimension
     * Pre-checks if there are any Question using this Dimension
     * @param id The ID of the Dimension
     */
    @Override
    @Transactional
    public void deleteDimension(UUID id, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        Dimension dimension = dimensionRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Dimension", "id", id, FormError.DIMENSION_NOT_FOUND));

        //check if any Question using the GroupDimension in this Dimension exists
        if (questionRepository.existsByGroupDimension_Dimension_Id(id)) {
            throw exceptionFactory.createCustomException("Dimension",
                    Collections.singletonList("id"),
                    Collections.singletonList(id),
                    FormError.DIMENSION_IN_USE);
        }

        dimensionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public DimensionDTO editDimensionName(UUID id, String name, UUID userAccountId){
        accessControlService.ensureBecamexRole(userAccountId);
        
        Dimension dimension = dimensionRepository.findById(id).orElseThrow(() ->
                exceptionFactory.createNotFoundException("Dimension", "id", id, FormError.DIMENSION_NOT_FOUND));

        dimension.setName(name);
        dimensionRepository.save(dimension);

        return dimensionMapper.toDTO(dimension);
    }

    private Dimension buildDimensionEntity(CreateDimensionDTO dto, SurveyForm surveyForm) {
        Dimension dimension = new Dimension();
        dimension.setName(dto.getName());
        dimension.setSurveyForm(surveyForm);

        if (dto.getGroupDimensions() != null && !dto.getGroupDimensions().isEmpty()) {
            List<GroupDimension> groupDimensions = dto.getGroupDimensions().stream()
                    .map(qDto -> groupDimensionService.buildGroupDimensionEntity(qDto, dimension))
                    .toList();
            dimension.setGroupDimensions(new HashSet<>(groupDimensions));
        }
        return dimension;
    }
}