package com.EIPplatform.mapper.historyAction;

import org.mapstruct.Mapper;

import com.EIPplatform.model.dto.historyAction.ActionDto;
import com.EIPplatform.model.entity.historyActions.Action;

@Mapper(componentModel = "spring")
public interface ActionMapper {
    ActionDto toActionDto(Action action);
    Action toAction(ActionDto actionDto);
}
