package com.EIPplatform.service.report.report06.part02;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ObjectError;
import com.EIPplatform.mapper.report.report06.part02.OperationalActivityDataMapper;
import com.EIPplatform.model.dto.report.report06.part2.OperationalActivityDataDto;
import com.EIPplatform.model.entity.report.report06.Report06;
import com.EIPplatform.model.entity.report.report06.part02.OperationalActivityData;
import com.EIPplatform.repository.report.report06.Report06Repository;
import com.EIPplatform.repository.report.report06.part02.OperationalActivityDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OperationActivityDataServiceImpl implements OperationActivityDataService {

    private final OperationalActivityDataRepository opDataRepo;
    private final Report06Repository reportRepo;
    private final OperationalActivityDataMapper mapper;
    private final ExceptionFactory exceptionFactory;

    @Override
    public OperationalActivityDataDto findById(UUID id) {
        return opDataRepo.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "OperationalActivityData", "id", ObjectError.ENTITY_NOT_FOUND
                ));
    }

    @Override
    public OperationalActivityDataDto create(OperationalActivityDataDto dto, UUID reportId) {
        Report06 report = reportRepo.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Report06", "id", ObjectError.ENTITY_NOT_FOUND));

        OperationalActivityData entity = mapper.toEntity(dto);
        entity.setReport06(report);

        // Default initial values (required)
        entity.setDataManagementProcedure("Not Provided");
        entity.setEmissionFactorSource("Not Provided");

        entity = opDataRepo.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public OperationalActivityDataDto update(UUID id, OperationalActivityDataDto dto) {
        OperationalActivityData entity = opDataRepo.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "OperationalActivityData", "id", ObjectError.ENTITY_NOT_FOUND));

        mapper.updateEntity(entity, dto);

        entity = opDataRepo.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public void delete(UUID id) {
        OperationalActivityData entity = opDataRepo.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "OperationalActivityData", "id", ObjectError.ENTITY_NOT_FOUND));

        opDataRepo.delete(entity);
    }
}
