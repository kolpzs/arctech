package com.arctech.users.services;

import com.arctech.users.entities.User;
import com.arctech.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User findOrCreateUserFromJwt(Jwt jwt) {
        String keycloakId = jwt.getSubject(); // O "sub" do token é o ID do usuário no Keycloak

        Optional<User> userOptional = userRepository.findByKeycloakId(keycloakId);

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            User newUser = new User();
            newUser.setKeycloakId(keycloakId);
            newUser.setName(jwt.getClaimAsString("preferred_username"));
            newUser.setEmail(jwt.getClaimAsString("email"));
            return userRepository.save(newUser);
        }
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }
}