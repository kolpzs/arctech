package com.arctech.services;

import com.arctech.entities.TaskGroup;
import com.arctech.entities.User;
import com.arctech.repositories.TaskGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TaskGroupService {

    @Autowired
    private TaskGroupRepository taskGroupRepository;

    @Autowired
    private UserService userService; // Usaremos o serviço que sincroniza com o Keycloak

    // Lógica para criar um novo grupo
    public TaskGroup createGroup(TaskGroup group, User owner) {
        group.setOwner(owner);
        group.setMembers(Set.of(owner)); // O criador é o primeiro membro
        return taskGroupRepository.save(group);
    }

    public List<TaskGroup> findGroupsForUser(User user) {
        return taskGroupRepository.findByMembersContains(user);
    }

    public TaskGroup addMember(Long groupId, User userToAdd, User requester) {
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado."));

        if (!group.getOwner().equals(requester)) {
            throw new RuntimeException("Apenas o dono do grupo pode adicionar membros.");
        }

        group.getMembers().add(userToAdd);
        return taskGroupRepository.save(group);
    }
}