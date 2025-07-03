package com.arctech.controllers;

import com.arctech.dto.TaskDto;
import com.arctech.entities.Task;
import com.arctech.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lists/{listId}/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDto> createTask(
            @PathVariable Long listId,
            @Valid @RequestBody TaskDto taskDto,
            @AuthenticationPrincipal Jwt jwt) {

        Task createdTask = taskService.createTask(listId, taskDto, jwt);
        return new ResponseEntity<>(new TaskDto(createdTask), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasksFromList(@PathVariable Long listId, @AuthenticationPrincipal Jwt jwt) {
        List<Task> tasks = taskService.getTasksByList(listId, jwt);

        List<TaskDto> taskDtos = tasks.stream()
                .map(TaskDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(taskDtos);
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskDto taskUpdateDto,
            @AuthenticationPrincipal Jwt jwt) {

        Task updatedTask = taskService.updateTask(taskId, taskUpdateDto, jwt);

        return ResponseEntity.ok(new TaskDto(updatedTask));
    }

    @PatchMapping("/{taskId}/favorite")
    public ResponseEntity<TaskDto> toggleFavoriteStatus(
            @PathVariable Long taskId,
            @AuthenticationPrincipal Jwt jwt) {

        Task updatedTask = taskService.toggleFavoriteStatus(taskId, jwt);
        return ResponseEntity.ok(new TaskDto(updatedTask));
    }
}