package com.adrabazha.gypsy.board.repository;

import com.adrabazha.gypsy.board.domain.sql.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
