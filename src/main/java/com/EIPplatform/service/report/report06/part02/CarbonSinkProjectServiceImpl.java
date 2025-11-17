package com.EIPplatform.service.report.report06.part02;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ObjectError;
import com.EIPplatform.mapper.report.report06.part02.CarbonSinkProjectMapper;
import com.EIPplatform.model.dto.report.report06.part2.CarbonSinkProjectDto;
import com.EIPplatform.model.entity.report.report06.part02.CarbonSinkProject;
import com.EIPplatform.model.entity.report.report06.part02.OperationalActivityData;
import com.EIPplatform.repository.report.report06.part02.CarbonSinkProjectRepository;
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
public class CarbonSinkProjectServiceImpl implements CarbonSinkProjectService {

    private final CarbonSinkProjectRepository carbonRepo;
    private final OperationalActivityDataRepository opDataRepo;
    private final CarbonSinkProjectMapper mapper;
    private final ExceptionFactory exceptionFactory;

    @Override
    public CarbonSinkProjectDto create(UUID opDataId, CarbonSinkProjectDto dto) {
        OperationalActivityData opData = opDataRepo.findById(opDataId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "OperationalActivityData", "id", ObjectError.ENTITY_NOT_FOUND));

        CarbonSinkProject entity = mapper.toEntity(dto);
        entity.setOperationalActivityData(opData);

        entity = carbonRepo.save(entity);

        return mapper.toDto(entity);
    }

    @Override
    public CarbonSinkProjectDto update(UUID id, CarbonSinkProjectDto dto) {
        CarbonSinkProject entity = carbonRepo.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "CarbonSinkProject", "id", ObjectError.ENTITY_NOT_FOUND));

        mapper.updateEntity(entity, dto);

        entity = carbonRepo.save(entity);

        return mapper.toDto(entity);
    }

    @Override
    public void delete(UUID id) {
        CarbonSinkProject entity = carbonRepo.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "CarbonSinkProject", "id", ObjectError.ENTITY_NOT_FOUND));

        carbonRepo.delete(entity);
    }

    @Override
    public CarbonSinkProjectDto findById(UUID id) {
        return carbonRepo.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "CarbonSinkProject", "id", ObjectError.ENTITY_NOT_FOUND));
    }

    @Override
    public List<CarbonSinkProjectDto> findByOperationalActivityData(UUID opDataId) {
        return mapper.toDtoList(
                carbonRepo.findByOperationalActivityData_OperationalActivityDataId(opDataId)
        );
    }
}
