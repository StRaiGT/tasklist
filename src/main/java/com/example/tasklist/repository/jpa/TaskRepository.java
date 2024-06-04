package com.example.tasklist.repository.jpa;

import com.example.tasklist.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByOwnerId(Long userId);

    List<Task> getAllByExpirationDateBetween(
            LocalDateTime start,
            LocalDateTime end
    );
}
