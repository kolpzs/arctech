package com.arctech.controllers;

import com.arctech.dto.TaskGroupDto;
import com.arctech.entities.TaskGroup;
import com.arctech.entities.User;
import com.arctech.services.TaskGroupService;
import com.arctech.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class TaskGroupController {

    @Autowired
    private TaskGroupService taskGroupService;

    @Autowired
    private UserService userService;

    private User getLocalUser(Jwt jwt) {
        return userService.synchronizeUser(jwt);
    }

    @PostMapping
    public ResponseEntity<TaskGroup> createGroup(@RequestBody TaskGroupDto groupDto, @AuthenticationPrincipal Jwt jwt) {
        User owner = getLocalUser(jwt);
        TaskGroup newGroup = new TaskGroup();
        newGroup.setName(groupDto.getName());
        TaskGroup createdGroup = taskGroupService.createGroup(newGroup, owner);
        return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskGroup>> getMyGroups(@AuthenticationPrincipal Jwt jwt) {
        User user = getLocalUser(jwt);
        List<TaskGroup> groups = taskGroupService.findGroupsForUser(user);
        return ResponseEntity.ok(groups);
    }
}