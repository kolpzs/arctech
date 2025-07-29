package com.arctech.tasks.dto;

import com.arctech.tasks.entities.TaskGroup;
import com.arctech.users.dto.UserDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class TaskGroupDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotBlank(message = "O nome do grupo não pode ser vazio.")
    private String name;
    private UserDto owner;
    private Set<UserDto> members;

    public TaskGroupDto(TaskGroup entity) {
        this.id = entity.getId();
        this.name = entity.getName();

        if (entity.getOwner() != null) {
            this.owner = new UserDto(entity.getOwner());
        }

        if (entity.getMembers() != null) {
            this.members = entity.getMembers().stream()
                    .map(UserDto::new)
                    .collect(Collectors.toSet());
        } else {
            this.members = Collections.emptySet();
        }
    }
}