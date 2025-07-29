package com.arctech.tasks.controllers;

import com.arctech.tasks.dto.TaskGroupDto;
import com.arctech.tasks.entities.TaskGroup;
import com.arctech.tasks.services.TaskGroupService;
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
@RequestMapping("/api/task-group")
public class TaskGroupController {

    @Autowired
    private TaskGroupService taskGroupService;

    @PostMapping("/create")
    public ResponseEntity<TaskGroupDto> createGroup(@Valid @RequestBody TaskGroupDto groupDto, @AuthenticationPrincipal Jwt jwt) {
        TaskGroup groupToCreate = new TaskGroup();
        groupToCreate.setName(groupDto.getName());
        TaskGroup createdGroup = taskGroupService.createGroup(groupToCreate, jwt);
        return new ResponseEntity<>(new TaskGroupDto(createdGroup), HttpStatus.CREATED);
    }

    @GetMapping("/listByGroup")
    public ResponseEntity<List<TaskGroupDto>> getMyGroups(@AuthenticationPrincipal Jwt jwt) {
        List<TaskGroup> groups = taskGroupService.findGroupsForCurrentUser(jwt);
        List<TaskGroupDto> groupDtos = groups.stream().map(TaskGroupDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(groupDtos);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<TaskGroupDto> removeMember(
            @RequestParam Long grupoId,
            @RequestParam Long usuarioId,
            @AuthenticationPrincipal Jwt jwt) {

        TaskGroup updatedGroup = taskGroupService.removeMember(grupoId, usuarioId, jwt);
        return ResponseEntity.ok(new TaskGroupDto(updatedGroup));
    }

    @DeleteMapping("/leave")
    public ResponseEntity<Void> leaveGroup(@RequestParam Long grupoId, @AuthenticationPrincipal Jwt jwt) { // Usando @RequestParam
        taskGroupService.leaveGroup(grupoId, jwt);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteGroup(@RequestParam Long grupoId, @AuthenticationPrincipal Jwt jwt) { // Usando @RequestParam
        taskGroupService.deleteGroup(grupoId, jwt);
        return ResponseEntity.noContent().build();
    }
}