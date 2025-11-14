package com.EIPplatform.service.report.report06.part02;

import com.EIPplatform.model.dto.report.report06.part2.OperationalActivityDataDto;

import java.util.UUID;

public interface OperationActivityDataService {

    OperationalActivityDataDto findById(UUID id);

    OperationalActivityDataDto create(OperationalActivityDataDto dto, UUID reportId);

    OperationalActivityDataDto update(UUID id, OperationalActivityDataDto dto);

    void delete(UUID id);
}
