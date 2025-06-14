package com.arctech.repositories;

import com.arctech.entities.TaskGroup;
import com.arctech.entities.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    List<TaskList> findByGroup(TaskGroup group);
}