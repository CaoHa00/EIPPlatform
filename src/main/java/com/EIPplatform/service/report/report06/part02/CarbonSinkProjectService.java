package com.EIPplatform.service.report.report06.part02;

import com.EIPplatform.model.dto.report.report06.part2.CarbonSinkProjectDto;

import java.util.List;
import java.util.UUID;

public interface CarbonSinkProjectService {

    CarbonSinkProjectDto create(UUID opDataId, CarbonSinkProjectDto dto);

    CarbonSinkProjectDto update(UUID id, CarbonSinkProjectDto dto);

    void delete(UUID id);

    CarbonSinkProjectDto findById(UUID id);

    List<CarbonSinkProjectDto> findByOperationalActivityData(UUID opDataId);
}
