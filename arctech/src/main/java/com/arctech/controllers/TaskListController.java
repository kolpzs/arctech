package com.arctech.controllers;

import com.arctech.dto.TaskListDto;
import com.arctech.entities.TaskList;
import com.arctech.entities.User;
import com.arctech.services.TaskListService;
import com.arctech.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/groups/{groupId}/lists")
public class TaskListController {

    @Autowired
    private TaskListService taskListService;

    @Autowired
    private UserService userService;

    private User getLocalUser(Jwt jwt) {
        return userService.synchronizeUser(jwt);
    }

    @PostMapping
    public ResponseEntity<TaskList> createTaskList(@PathVariable Long groupId, @RequestBody TaskListDto listDto, @AuthenticationPrincipal Jwt jwt) {
        User requester = getLocalUser(jwt);
        TaskList newTaskList = new TaskList();
        newTaskList.setName(listDto.getName());
        TaskList createdList = taskListService.createTaskList(groupId, newTaskList, requester);
        return new ResponseEntity<>(createdList, HttpStatus.CREATED);
    }
}