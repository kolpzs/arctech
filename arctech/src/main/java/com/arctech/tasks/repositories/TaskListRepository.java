package com.arctech.tasks.repositories;

import com.arctech.tasks.entities.TaskGroup;
import com.arctech.tasks.entities.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    List<TaskList> findByGroup(TaskGroup group);
}