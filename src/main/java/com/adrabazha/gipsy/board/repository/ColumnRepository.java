package com.adrabazha.gipsy.board.repository;

import com.adrabazha.gipsy.board.domain.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnRepository extends JpaRepository<BoardColumn, Long> {
}
