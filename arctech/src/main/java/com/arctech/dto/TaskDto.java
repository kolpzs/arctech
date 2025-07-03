package com.arctech.dto;

import com.arctech.entities.Task;
import com.arctech.entities.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TaskDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotBlank(message = "O título da tarefa não pode ser vazio.")
    private String title;
    private String description;
    private TaskStatus status;
    private boolean favorited;
    private LocalDateTime dueDate;
    private Long taskListId;
    private LocalDateTime createdAt;
    private String createdBy;
    private Long assigneeId;
    private String assigneeName;

    public TaskDto(Task entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.description = entity.getDescription();
        this.status = entity.getStatus();
        this.favorited = entity.isFavorited();
        this.dueDate = entity.getDueDate();
        this.createdAt = entity.getCreatedAt();
        this.createdBy = entity.getCreatedBy();

        if (entity.getTaskList() != null) {
            this.taskListId = entity.getTaskList().getId();
        }

        if (entity.getAssignee() != null) {
            this.assigneeId = entity.getAssignee().getId();
            this.assigneeName = entity.getAssignee().getName();
        }
    }
}