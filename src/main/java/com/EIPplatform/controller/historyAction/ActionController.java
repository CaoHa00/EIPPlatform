package com.EIPplatform.controller.historyAction;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EIPplatform.annotations.RateLimit;
import com.EIPplatform.model.dto.historyAction.ActionDto;
import com.EIPplatform.service.historyAction.ActionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
@RateLimit(value = "default")
public class ActionController {
    private final ActionService actionService;

    // Create
    @PostMapping
    public ResponseEntity<ActionDto> addAction(@Valid @RequestBody ActionDto actionDto) {
        ActionDto created = actionService.addAction(actionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<ActionDto> updateAction(@PathVariable Long id,
            @Valid @RequestBody ActionDto actionDto) {
        ActionDto updated = actionService.updateActionDto(actionDto, id);
        return ResponseEntity.ok(updated);
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<ActionDto> getActionById(@PathVariable Long id) {
        ActionDto action = actionService.getActionById(id);
        return ResponseEntity.ok(action);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAction(@PathVariable Long id) {
        actionService.deleteActionType(id); // hoặc đổi tên thành deleteAction
        return ResponseEntity.noContent().build();
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<ActionDto>> getAllActions() {
        List<ActionDto> actions = actionService.getAllAction();
        return ResponseEntity.ok(actions);
    }
}
