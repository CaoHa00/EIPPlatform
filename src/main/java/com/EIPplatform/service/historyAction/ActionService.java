package com.EIPplatform.service.historyAction;

import java.util.List;

import com.EIPplatform.model.dto.historyAction.ActionDto;

public interface ActionService {
    ActionDto addAction(ActionDto actionDto);

    ActionDto updateActionDto(ActionDto equipmentProviderDto, Long id);

    ActionDto getActionById(Long id);

    void deleteActionType(Long id);

    List<ActionDto> getAllAction();
}
