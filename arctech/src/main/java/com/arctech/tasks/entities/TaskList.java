package com.arctech.tasks.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "task_lists")
@Getter
@Setter
@NoArgsConstructor
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // A qual grupo esta lista pertence
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    @JsonIgnore
    private TaskGroup group;

    // As tarefas contidas nesta lista
    @OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;
}