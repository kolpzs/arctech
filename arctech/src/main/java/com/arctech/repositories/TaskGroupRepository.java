package com.arctech.repositories;

import com.arctech.entities.TaskGroup;
import com.arctech.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskGroupRepository extends JpaRepository<TaskGroup, Long> {
    List<TaskGroup> findByMembersContains(User member);
}