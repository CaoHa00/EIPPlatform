package com.EIPplatform.service.report.report06.part02;

import com.EIPplatform.model.dto.report.report06.part2.LimitationDto;

import java.util.List;
import java.util.UUID;

public interface LimitationService {

    LimitationDto create(UUID opDataId, LimitationDto dto);

    LimitationDto update(UUID id, LimitationDto dto);

    void delete(UUID id);

    LimitationDto findById(UUID id);

    List<LimitationDto> findByOperationalActivityData(UUID opDataId);
}
