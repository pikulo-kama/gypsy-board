package com.adrabazha.gipsy.board.repository;

import com.adrabazha.gipsy.board.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
