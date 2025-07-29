package com.arctech.tasks.repositories;

import com.arctech.tasks.entities.Task;
import com.arctech.tasks.entities.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.taskList = :taskList " +
            "ORDER BY t.favorited DESC, t.dueDate ASC, t.createdAt ASC")
    List<Task> findByTaskListSorted(@Param("taskList") TaskList taskList);
}