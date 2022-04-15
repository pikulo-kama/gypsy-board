package com.adrabazha.gypsy.board.repository;

import com.adrabazha.gypsy.board.domain.sql.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
}
