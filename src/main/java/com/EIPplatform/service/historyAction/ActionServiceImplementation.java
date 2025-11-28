package com.EIPplatform.service.historyAction;

import java.util.List;

import org.springframework.stereotype.Service;

import com.EIPplatform.mapper.historyAction.ActionMapper;
import com.EIPplatform.model.dto.historyAction.ActionDto;
import com.EIPplatform.model.entity.historyActions.Action;
import com.EIPplatform.repository.historyAction.ActionRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActionServiceImplementation implements ActionService {
    final ActionRepository actionRepository;
    final ActionMapper actionMapper;

    @Override
    public ActionDto addAction(ActionDto actionDto) {
        Action action = actionMapper.toAction(actionDto);
        Action savedAction = actionRepository.save(action);
        return actionMapper.toActionDto(savedAction);
    }

    @Override
    public ActionDto updateActionDto(ActionDto actionDto, Long id) {
        Action existingAction = actionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Action not found with id: " + id));

        existingAction.setActionName(actionDto.getActionName());

        Action updatedAction = actionRepository.save(existingAction);
        return actionMapper.toActionDto(updatedAction);
    }

    @Override
    public ActionDto getActionById(Long id) {
        Action action = actionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Action not found with id: " + id));

        return actionMapper.toActionDto(action);
    }

    @Override
    public void deleteActionType(Long id) {
        if (!actionRepository.existsById(id)) {
            throw new RuntimeException("Action not found with id: " + id);
        }
        actionRepository.deleteById(id);
    }

    @Override
    public List<ActionDto> getAllAction() {
        return actionRepository.findAll()
                .stream()
                .map(actionMapper::toActionDto)
                .toList();
    }

}
