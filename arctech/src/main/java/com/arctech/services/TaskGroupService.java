package com.arctech.services;

import com.arctech.entities.TaskGroup;
import com.arctech.entities.User;
import com.arctech.exceptions.ForbiddenException;
import com.arctech.exceptions.ResourceNotFoundException;
import com.arctech.repositories.TaskGroupRepository;
import com.arctech.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class TaskGroupService {

    @Autowired
    private TaskGroupRepository taskGroupRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public TaskGroup createGroup(TaskGroup group, Jwt jwt) {
        User owner = userService.findOrCreateUserFromJwt(jwt);

        group.setOwner(owner);
        group.setMembers(Set.of(owner));
        return taskGroupRepository.save(group);
    }

    @Transactional(readOnly = true)
    public List<TaskGroup> findGroupsForCurrentUser(Jwt jwt) {
        User currentUser = userService.findOrCreateUserFromJwt(jwt);
        return taskGroupRepository.findByMembersContains(currentUser);
    }

    @Transactional
    public TaskGroup addMember(Long groupId, String userEmailToAdd, Jwt requesterJwt) {
        User requester = userService.findOrCreateUserFromJwt(requesterJwt);

        User userToAdd = userRepository.findByEmail(userEmailToAdd)
                .orElseThrow(() -> new RuntimeException("Usuário com email " + userEmailToAdd + " não encontrado."));

        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado."));

        if (!group.getOwner().equals(requester)) {
            throw new RuntimeException("Apenas o dono do grupo pode adicionar membros.");
        }

        group.getMembers().add(userToAdd);
        return taskGroupRepository.save(group);
    }

    @Transactional
    public TaskGroup removeMember(Long groupId, Long userIdToRemove, Jwt requesterJwt) {
        User requester = userService.findOrCreateUserFromJwt(requesterJwt);
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado com ID: " + groupId));

        if (!group.getOwner().equals(requester)) {
            throw new ForbiddenException("Apenas o dono do grupo pode remover membros.");
        }

        if (requester.getId().equals(userIdToRemove)) {
            throw new ForbiddenException("O dono não pode se remover. Para excluir o grupo, utilize a rota de exclusão de grupo.");
        }

        User userToRemove = userRepository.findById(userIdToRemove)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário a ser removido não encontrado com ID: " + userIdToRemove));

        if (!group.getMembers().contains(userToRemove)) {
            throw new ResourceNotFoundException("Usuário não é membro deste grupo.");
        }

        group.getMembers().remove(userToRemove);
        return taskGroupRepository.save(group);
    }

    @Transactional
    public void leaveGroup(Long groupId, Jwt requesterJwt) {
        User requester = userService.findOrCreateUserFromJwt(requesterJwt);
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado com ID: " + groupId));

        if (group.getOwner().equals(requester)) {
            throw new ForbiddenException("Você é o dono e não pode usar a função 'sair'. Para excluir o grupo, utilize a rota de exclusão.");
        }

        if (!group.getMembers().contains(requester)) {
            throw new ForbiddenException("Acesso negado. Você não é membro deste grupo.");
        }

        group.getMembers().remove(requester);
        taskGroupRepository.save(group);
    }

    @Transactional
    public void deleteGroup(Long groupId, Jwt requesterJwt) {
        User requester = userService.findOrCreateUserFromJwt(requesterJwt);
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado com ID: " + groupId));

        if (!group.getOwner().equals(requester)) {
            throw new ForbiddenException("Apenas o dono pode deletar o grupo.");
        }

        if (group.getMembers().size() > 1) {
            throw new ForbiddenException("Para deletar o grupo, você deve primeiro remover todos os outros membros.");
        }

        taskGroupRepository.delete(group);
    }
}