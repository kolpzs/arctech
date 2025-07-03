package com.arctech.controllers;

import com.arctech.dto.TaskListDto;
import com.arctech.entities.TaskList;
import com.arctech.services.TaskListService;
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
@RequestMapping("/api/groups/{groupId}/lists")
public class TaskListController {

    @Autowired
    private TaskListService taskListService;

    @PostMapping
    public ResponseEntity<TaskListDto> createTaskList(
            @PathVariable Long groupId,
            @Valid @RequestBody TaskListDto listDto,
            @AuthenticationPrincipal Jwt jwt) {

        TaskList newTaskList = new TaskList();
        newTaskList.setName(listDto.getName());

        TaskList createdList = taskListService.createTaskList(groupId, newTaskList, jwt);
        return new ResponseEntity<>(new TaskListDto(createdList), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskListDto>> findAllByGroup(
            @PathVariable Long groupId,
            @AuthenticationPrincipal Jwt jwt) {

        List<TaskList> lists = taskListService.findAllByGroup(groupId, jwt);
        List<TaskListDto> dtos = lists.stream()
                .map(TaskListDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{listId}")
    public ResponseEntity<TaskListDto> updateTaskList(
            @PathVariable Long groupId,
            @PathVariable Long listId,
            @Valid @RequestBody TaskListDto listDto,
            @AuthenticationPrincipal Jwt jwt) {

        TaskList updatedList = taskListService.update(listId, listDto, jwt);
        return ResponseEntity.ok(new TaskListDto(updatedList));
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<Void> deleteTaskList(
            @PathVariable Long groupId,
            @PathVariable Long listId,
            @AuthenticationPrincipal Jwt jwt) {

        taskListService.delete(listId, jwt);
        return ResponseEntity.noContent().build();
    }
}