package com.arctech.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String keycloakId;

    @Column(unique = true, nullable = false)
    private String username;

    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    private Set<TaskGroup> groups = new HashSet<>();

    public User(String keycloakId, String username) {
        this.keycloakId = keycloakId;
        this.username = username;
    }
}