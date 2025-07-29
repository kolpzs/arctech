package com.arctech.tasks.controllers;

import com.arctech.tasks.dto.TaskDto;
import com.arctech.tasks.entities.Task;
import com.arctech.tasks.services.TaskService;
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
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<TaskDto> createTask(
            @RequestParam Long listaId,
            @Valid @RequestBody TaskDto taskDto,
            @AuthenticationPrincipal Jwt jwt) {

        Task createdTask = taskService.createTask(listaId, taskDto, jwt);
        return new ResponseEntity<>(new TaskDto(createdTask), HttpStatus.CREATED);
    }

    @GetMapping("/listByList")
    public ResponseEntity<List<TaskDto>> getTasksFromList(
            @RequestParam Long listaId,
            @AuthenticationPrincipal Jwt jwt) {
        List<Task> tasks = taskService.getTasksByList(listaId, jwt);
        List<TaskDto> taskDtos = tasks.stream().map(TaskDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(taskDtos);
    }

    @PatchMapping("/update")
    public ResponseEntity<TaskDto> updateTask(
            @RequestParam Long tarefaId,
            @RequestBody TaskDto taskUpdateDto,
            @AuthenticationPrincipal Jwt jwt) {

        Task updatedTask = taskService.updateTask(tarefaId, taskUpdateDto, jwt);
        return ResponseEntity.ok(new TaskDto(updatedTask));
    }

    @PatchMapping("/favorite")
    public ResponseEntity<TaskDto> toggleFavoriteStatus(
            @RequestParam Long tarefaId,
            @AuthenticationPrincipal Jwt jwt) {

        Task updatedTask = taskService.toggleFavoriteStatus(tarefaId, jwt);
        return ResponseEntity.ok(new TaskDto(updatedTask));
    }
}