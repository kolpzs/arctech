package com.arctech.services;

import com.arctech.entities.Task;
import com.arctech.entities.TaskList;
import com.arctech.entities.TaskStatus;
import com.arctech.entities.User;
import com.arctech.repositories.TaskListRepository;
import com.arctech.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskListRepository taskListRepository; // Usado para validação

    // Lógica para criar uma tarefa
    public Task createTask(Long taskListId, Task task, User requester) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new RuntimeException("Lista de tarefas não encontrada."));

        // Validação: Garante que o usuário que está criando a tarefa pertence ao grupo da lista
        if (taskList.getGroup().getMembers().stream().noneMatch(member -> member.equals(requester))) {
            throw new RuntimeException("Usuário não tem permissão para adicionar tarefas nesta lista.");
        }

        task.setTaskList(taskList);
        return taskRepository.save(task);
    }

    // Lógica para buscar as tarefas de uma lista (já ordenadas)
    public List<Task> getTasksByList(Long taskListId) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new RuntimeException("Lista de tarefas não encontrada."));
        return taskRepository.findByTaskListSorted(taskList);
    }

    // Lógica para atualizar o status de uma tarefa
    public Task updateTaskStatus(Long taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada."));
        task.setStatus(status);
        // A auditoria registrará quem e quando fez essa alteração
        return taskRepository.save(task);
    }
}