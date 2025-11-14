package com.EIPplatform.service.report.report06.part02;

import com.EIPplatform.model.dto.report.report06.part2.EmissionSourceDto;

import java.util.List;
import java.util.UUID;

public interface EmissionSourceService {
    EmissionSourceDto create(UUID opDataId, EmissionSourceDto dto);

    EmissionSourceDto update(UUID id, EmissionSourceDto dto);

    void delete(UUID id);

    EmissionSourceDto findById(UUID id);

    List<EmissionSourceDto> findByOperationalActivityData(UUID opDataId);
}
