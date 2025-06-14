package com.arctech.repositories;

import com.arctech.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Busca pelo ID do Keycloak, que é o campo 'sub' no token JWT
    Optional<User> findByKeycloakId(String keycloakId);
}