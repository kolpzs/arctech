package com.arctech.services;

import com.arctech.dto.TaskDto;
import com.arctech.entities.Task;
import com.arctech.entities.TaskList;
import com.arctech.entities.User;
import com.arctech.repositories.TaskListRepository;
import com.arctech.repositories.TaskRepository;
import com.arctech.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Task createTask(Long listId, TaskDto taskDto, Jwt jwt) {
        TaskList taskList = findListAndVerifyAccess(listId, jwt);

        Task newTask = new Task();
        newTask.setTitle(taskDto.getTitle());
        newTask.setDescription(taskDto.getDescription());
        newTask.setDueDate(taskDto.getDueDate());
        if (taskDto.getStatus() != null) {
            newTask.setStatus(taskDto.getStatus());
        }
        newTask.setTaskList(taskList);

        if (taskDto.getAssigneeId() != null) {
            User assignee = userRepository.findById(taskDto.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Usuário responsável não encontrado com ID: " + taskDto.getAssigneeId()));

            if (!taskList.getGroup().getMembers().contains(assignee)) {
                throw new RuntimeException("Não é possível atribuir a tarefa a um usuário que não é membro do grupo.");
            }
            newTask.setAssignee(assignee);
        }

        return taskRepository.save(newTask);
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByList(Long listId, Jwt jwt) {
        TaskList taskList = findListAndVerifyAccess(listId, jwt); // Verificação de segurança
        return taskRepository.findByTaskListSorted(taskList);
    }

    @Transactional
    public Task updateTask(Long taskId, TaskDto taskUpdateDto, Jwt jwt) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task não encontrada com ID: " + taskId));

        TaskList taskList = findListAndVerifyAccess(task.getTaskList().getId(), jwt);

        if (taskUpdateDto.getTitle() != null) {
            task.setTitle(taskUpdateDto.getTitle());
        }
        if (taskUpdateDto.getDescription() != null) {
            task.setDescription(taskUpdateDto.getDescription());
        }
        if (taskUpdateDto.getDueDate() != null) {
            task.setDueDate(taskUpdateDto.getDueDate());
        }
        if (taskUpdateDto.getStatus() != null) {
            task.setStatus(taskUpdateDto.getStatus());
        }
        task.setFavorited(taskUpdateDto.isFavorited());

        if (taskUpdateDto.getAssigneeId() != null) {
            User assignee = userRepository.findById(taskUpdateDto.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Usuário responsável não encontrado com ID: " + taskUpdateDto.getAssigneeId()));

            if (!taskList.getGroup().getMembers().contains(assignee)) {
                throw new RuntimeException("Não é possível atribuir a tarefa a um usuário que não é membro do grupo.");
            }
            task.setAssignee(assignee);
        }

        return taskRepository.save(task);
    }

    private TaskList findListAndVerifyAccess(Long listId, Jwt jwt) {
        User requester = userService.findOrCreateUserFromJwt(jwt);

        TaskList taskList = taskListRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("Lista de tarefas não encontrada com ID: " + listId));

        if (!taskList.getGroup().getMembers().contains(requester)) {
            throw new RuntimeException("Acesso negado. Usuário não é membro do grupo proprietário desta lista.");
        }

        return taskList;
    }

    @Transactional
    public Task toggleFavoriteStatus(Long taskId, Jwt jwt) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task não encontrada com ID: " + taskId));
        findListAndVerifyAccess(task.getTaskList().getId(), jwt);
        task.setFavorited(!task.isFavorited());
        return taskRepository.save(task);
    }
}