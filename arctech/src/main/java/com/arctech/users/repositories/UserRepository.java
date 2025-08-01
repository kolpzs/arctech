package com.arctech.users.repositories;

import com.arctech.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Busca pelo ID do Keycloak, que é o campo 'sub' no token JWT
    Optional<User> findByKeycloakId(String keycloakId);

    Optional<User> findByEmail(String email);
}