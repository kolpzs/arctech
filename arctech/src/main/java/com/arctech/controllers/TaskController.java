package com.arctech.controllers;

import com.arctech.dto.TaskDto;
import com.arctech.entities.Task;
import com.arctech.entities.User;
import com.arctech.services.TaskService;
import com.arctech.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists/{listId}/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    private User getLocalUser(Jwt jwt) {
        return userService.synchronizeUser(jwt);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@PathVariable Long listId, @RequestBody TaskDto taskDto, @AuthenticationPrincipal Jwt jwt) {
        User requester = getLocalUser(jwt);
        Task newTask = new Task();
        newTask.setTitle(taskDto.getTitle());
        newTask.setDescription(taskDto.getDescription());
        newTask.setDueDate(taskDto.getDueDate());

        Task createdTask = taskService.createTask(listId, newTask, requester);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasksFromList(@PathVariable Long listId) {
        List<Task> tasks = taskService.getTasksByList(listId);
        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody TaskDto taskUpdateDto) {
        // Este método pode ser expandido no TaskService para atualizar diferentes campos
        // Por agora, vamos focar em atualizar o status
        if (taskUpdateDto.getStatus() != null) {
            Task updatedTask = taskService.updateTaskStatus(taskId, taskUpdateDto.getStatus());
            return ResponseEntity.ok(updatedTask);
        }
        // Lógica para favoritar pode ser adicionada aqui também
        return ResponseEntity.badRequest().build();
    }
}