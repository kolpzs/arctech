package com.arctech.services;

import com.arctech.entities.Conta;
import com.arctech.entities.User;
import com.arctech.repositories.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private UserService userService; // Injeta o novo serviço

    // Helper para pegar o usuário logado a partir do token JWT
    private User getAuthenticatedUser() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.synchronizeUser(jwt);
    }

    public Conta save(Conta conta) {
        User currentUser = getAuthenticatedUser();
        conta.setUser(currentUser); // Associa a conta ao usuário sincronizado
        return contaRepository.save(conta);
    }

    public List<Conta> findAll() {
        User currentUser = getAuthenticatedUser();
        return contaRepository.findByUser(currentUser);
    }

    public Optional<Conta> findById(Long id) {
        User currentUser = getAuthenticatedUser();
        return contaRepository.findByIdAndUser(id, currentUser);
    }

    // ... os outros métodos (update, delete, etc.) continuam usando a mesma lógica
    // de chamar getAuthenticatedUser() primeiro, então eles já funcionarão corretamente.
}