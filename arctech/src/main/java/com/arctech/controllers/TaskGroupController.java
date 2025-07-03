package com.arctech.controllers;

import com.arctech.dto.TaskGroupDto;
import com.arctech.entities.TaskGroup;
import com.arctech.services.TaskGroupService;
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
@RequestMapping("/api/groups")
public class TaskGroupController {

    @Autowired
    private TaskGroupService taskGroupService;

    @PostMapping
    public ResponseEntity<TaskGroupDto> createGroup(@Valid @RequestBody TaskGroupDto groupDto, @AuthenticationPrincipal Jwt jwt) {
        TaskGroup groupToCreate = new TaskGroup();
        groupToCreate.setName(groupDto.getName());
        TaskGroup createdGroup = taskGroupService.createGroup(groupToCreate, jwt);
        return new ResponseEntity<>(new TaskGroupDto(createdGroup), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskGroupDto>> getMyGroups(@AuthenticationPrincipal Jwt jwt) {
        List<TaskGroup> groups = taskGroupService.findGroupsForCurrentUser(jwt);

        List<TaskGroupDto> groupDtos = groups.stream()
                .map(TaskGroupDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(groupDtos);
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<TaskGroupDto> removeMember(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            @AuthenticationPrincipal Jwt jwt) {

        TaskGroup updatedGroup = taskGroupService.removeMember(groupId, userId, jwt);
        return ResponseEntity.ok(new TaskGroupDto(updatedGroup));
    }

    @DeleteMapping("/{groupId}/leave")
    public ResponseEntity<Void> leaveGroup(@PathVariable Long groupId, @AuthenticationPrincipal Jwt jwt) {
        taskGroupService.leaveGroup(groupId, jwt);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(
            @PathVariable Long groupId,
            @AuthenticationPrincipal Jwt jwt) {

        taskGroupService.deleteGroup(groupId, jwt);
        return ResponseEntity.noContent().build();
    }
}