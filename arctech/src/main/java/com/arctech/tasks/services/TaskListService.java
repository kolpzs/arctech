package com.arctech.tasks.services;

import com.arctech.exceptions.ForbiddenException;
import com.arctech.exceptions.ResourceNotFoundException;
import com.arctech.tasks.dto.TaskListDto;
import com.arctech.tasks.entities.TaskGroup;
import com.arctech.tasks.entities.TaskList;
import com.arctech.tasks.repositories.TaskGroupRepository;
import com.arctech.tasks.repositories.TaskListRepository;
import com.arctech.users.entities.User;
import com.arctech.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskListService {

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private TaskGroupRepository taskGroupRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public TaskList createTaskList(Long groupId, TaskList taskList, Jwt jwt) {
        User requester = userService.findOrCreateUserFromJwt(jwt);
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado com ID: " + groupId));

        if (!group.getMembers().contains(requester)) {
            throw new ForbiddenException("Acesso negado. Você não é membro deste grupo.");
        }

        taskList.setGroup(group);
        return taskListRepository.save(taskList);
    }

    @Transactional(readOnly = true)
    public List<TaskList> findAllByGroup(Long groupId, Jwt jwt) {
        User requester = userService.findOrCreateUserFromJwt(jwt);
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado com ID: " + groupId));

        if (!group.getMembers().contains(requester)) {
            throw new ForbiddenException("Acesso negado. Você não é membro deste grupo.");
        }

        return taskListRepository.findByGroup(group);
    }

    @Transactional
    public TaskList update(Long listId, TaskListDto listDto, Jwt jwt) {
        TaskList listToUpdate = findListAndVerifyAccess(listId, jwt);
        listToUpdate.setName(listDto.getName());
        return taskListRepository.save(listToUpdate);
    }

    @Transactional
    public void delete(Long listId, Jwt jwt) {
        TaskList listToDelete = findListAndVerifyAccess(listId, jwt);
        taskListRepository.delete(listToDelete);
    }

    private TaskList findListAndVerifyAccess(Long listId, Jwt jwt) {
        User requester = userService.findOrCreateUserFromJwt(jwt);

        TaskList taskList = taskListRepository.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("Lista de tarefas não encontrada com ID: " + listId));

        if (!taskList.getGroup().getMembers().contains(requester)) {
            throw new ForbiddenException("Acesso negado. Você não tem permissão para acessar esta lista de tarefas.");
        }

        return taskList;
    }
}