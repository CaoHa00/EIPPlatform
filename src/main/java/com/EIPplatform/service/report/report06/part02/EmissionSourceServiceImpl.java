package com.EIPplatform.service.report.report06.part02;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ObjectError;
import com.EIPplatform.mapper.report.report06.part02.EmissionSourceMapper;
import com.EIPplatform.model.dto.report.report06.part2.EmissionSourceDto;
import com.EIPplatform.model.entity.report.report06.part02.EmissionSource;
import com.EIPplatform.model.entity.report.report06.part02.OperationalActivityData;
import com.EIPplatform.repository.report.report06.part02.EmissionSourceRepository;
import com.EIPplatform.repository.report.report06.part02.OperationalActivityDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EmissionSourceServiceImpl implements EmissionSourceService {

    private final EmissionSourceRepository emissionSourceRepository;
    private final OperationalActivityDataRepository opDataRepo;
    private final EmissionSourceMapper mapper;
    private final ExceptionFactory exceptionFactory;

    @Override
    public EmissionSourceDto create(UUID opDataId, EmissionSourceDto dto) {
        OperationalActivityData opData = opDataRepo.findById(opDataId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "OperationalActivityData", "id", ObjectError.ENTITY_NOT_FOUND));

        EmissionSource entity = mapper.toEntity(dto);
        entity.setOperationalActivityData(opData);

        validateEmissionSource(entity);

        entity = emissionSourceRepository.save(entity);

        log.info("Created EmissionSource {}", entity.getEmissionSourceId());

        return mapper.toDto(entity);
    }

    @Override
    public EmissionSourceDto update(UUID id, EmissionSourceDto dto) {
        EmissionSource entity = emissionSourceRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "EmissionSource", "id", ObjectError.ENTITY_NOT_FOUND));

        mapper.updateEntity(entity, dto);
        validateEmissionSource(entity);

        entity = emissionSourceRepository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public void delete(UUID id) {
        EmissionSource entity = emissionSourceRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "EmissionSource", "id", ObjectError.ENTITY_NOT_FOUND));

        emissionSourceRepository.delete(entity);
    }

    @Override
    public EmissionSourceDto findById(UUID id) {
        return emissionSourceRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "EmissionSource", "id", ObjectError.ENTITY_NOT_FOUND));
    }

    @Override
    public List<EmissionSourceDto> findByOperationalActivityData(UUID opDataId) {
        return mapper.toDtoList(
                emissionSourceRepository.findByOperationalActivityData_OperationalActivityDataId(opDataId)
        );
    }

    private void validateEmissionSource(EmissionSource entity) {
        if (entity.getSourceScope() < 1 || entity.getSourceScope() > 3) {
            throw exceptionFactory.createValidationException(
                    "sourceScope",
                    "must be 1, 2, or 3",
                    entity.getSourceScope(),
                    ObjectError.BAD_REQUEST
            );
        }
    }
}
