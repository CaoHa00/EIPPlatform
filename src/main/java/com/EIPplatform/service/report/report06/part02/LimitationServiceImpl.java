package com.EIPplatform.service.report.report06.part02;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ObjectError;
import com.EIPplatform.mapper.report.report06.part02.LimitationMapper;
import com.EIPplatform.model.dto.report.report06.part2.LimitationDto;
import com.EIPplatform.model.entity.report.report06.part02.Limitation;
import com.EIPplatform.model.entity.report.report06.part02.OperationalActivityData;
import com.EIPplatform.repository.report.report06.part02.LimitationRepository;
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
public class LimitationServiceImpl implements LimitationService {

    private final LimitationRepository limitationRepo;
    private final OperationalActivityDataRepository opDataRepo;
    private final LimitationMapper mapper;
    private final ExceptionFactory exceptionFactory;

    @Override
    public LimitationDto create(UUID opDataId, LimitationDto dto) {
        OperationalActivityData opData = opDataRepo.findById(opDataId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "OperationalActivityData", "id", ObjectError.ENTITY_NOT_FOUND));

        Limitation entity = mapper.toEntity(dto);
        entity.setOperationalActivityData(opData);

        entity = limitationRepo.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public LimitationDto update(UUID id, LimitationDto dto) {
        Limitation entity = limitationRepo.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Limitation", "id", ObjectError.ENTITY_NOT_FOUND));

        mapper.updateEntity(entity, dto);

        entity = limitationRepo.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public void delete(UUID id) {
        Limitation entity = limitationRepo.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Limitation", "id", ObjectError.ENTITY_NOT_FOUND));

        limitationRepo.delete(entity);
    }

    @Override
    public LimitationDto findById(UUID id) {
        return limitationRepo.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Limitation", "id", ObjectError.ENTITY_NOT_FOUND));
    }

    @Override
    public List<LimitationDto> findByOperationalActivityData(UUID opDataId) {
        return mapper.toDtoList(
                limitationRepo.findByOperationalActivityData_OperationalActivityDataId(opDataId)
        );
    }
}
