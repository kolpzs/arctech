package com.arctech.tasks.repositories;

import com.arctech.tasks.entities.TaskGroup;
import com.arctech.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskGroupRepository extends JpaRepository<TaskGroup, Long> {
    List<TaskGroup> findByMembersContains(User member);
}