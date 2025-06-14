package com.arctech.services;

import com.arctech.entities.User;
import com.arctech.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User synchronizeUser(Jwt jwt) {
        String keycloakId = jwt.getSubject(); // O campo 'sub' é o ID do usuário no Keycloak
        String username = jwt.getClaim("preferred_username");

        // Procura pelo usuário no banco local. Se não encontrar, cria um novo.
        return userRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    User newUser = new User(keycloakId, username);
                    return userRepository.save(newUser);
                });
    }
}