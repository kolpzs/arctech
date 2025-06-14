package com.arctech.services;

import com.arctech.entities.TaskList;
import com.arctech.entities.TaskGroup;
import com.arctech.entities.User;
import com.arctech.repositories.TaskGroupRepository;
import com.arctech.repositories.TaskListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskListService {

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private TaskGroupRepository taskGroupRepository;

    public TaskList createTaskList(Long groupId, TaskList taskList, User requester) {
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado."));

        if (group.getMembers().stream().noneMatch(member -> member.equals(requester))) {
            throw new RuntimeException("Usuário não é membro deste grupo.");
        }

        taskList.setGroup(group);
        return taskListRepository.save(taskList);
    }
}