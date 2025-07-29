package com.arctech.tasks.dto;

import com.arctech.tasks.entities.TaskList;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class TaskListDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotBlank(message = "O nome da lista não pode ser vazio.") // <-- ADICIONE A ANOTAÇÃO
    private String name;

    public TaskListDto(TaskList entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }
}