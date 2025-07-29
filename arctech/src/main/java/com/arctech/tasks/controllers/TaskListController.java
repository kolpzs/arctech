package com.arctech.tasks.controllers;

import com.arctech.tasks.dto.TaskListDto;
import com.arctech.tasks.entities.TaskList;
import com.arctech.tasks.services.TaskListService;
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
@RequestMapping("/api/task-list")
public class TaskListController {

    @Autowired
    private TaskListService taskListService;

    @PostMapping("/create")
    public ResponseEntity<TaskListDto> createTaskList(
            @RequestParam Long grupoId,
            @Valid @RequestBody TaskListDto listDto,
            @AuthenticationPrincipal Jwt jwt) {

        TaskList newTaskList = new TaskList();
        newTaskList.setName(listDto.getName());
        TaskList createdList = taskListService.createTaskList(grupoId, newTaskList, jwt);
        return new ResponseEntity<>(new TaskListDto(createdList), HttpStatus.CREATED);
    }

    @GetMapping("/listByGroup")
    public ResponseEntity<List<TaskListDto>> findAllByGroup(
            @RequestParam Long grupoId, // A busca é uma filtragem por grupo
            @AuthenticationPrincipal Jwt jwt) {

        List<TaskList> lists = taskListService.findAllByGroup(grupoId, jwt);
        List<TaskListDto> dtos = lists.stream().map(TaskListDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/update")
    public ResponseEntity<TaskListDto> updateTaskList(
            @RequestParam Long listaId, // Identificador da lista
            @Valid @RequestBody TaskListDto listDto,
            @AuthenticationPrincipal Jwt jwt) {

        TaskList updatedList = taskListService.update(listaId, listDto, jwt);
        return ResponseEntity.ok(new TaskListDto(updatedList));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteTaskList(
            @RequestParam Long listaId, // Identificador da lista
            @AuthenticationPrincipal Jwt jwt) {

        taskListService.delete(listaId, jwt);
        return ResponseEntity.noContent().build();
    }
}