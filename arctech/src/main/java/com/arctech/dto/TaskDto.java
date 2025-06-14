package com.arctech.dto;

import com.arctech.entities.TaskStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDto {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private TaskStatus status;
    private Boolean favorited;
}