package com.adrabazha.gypsy.board.repository;

import com.adrabazha.gypsy.board.domain.sql.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Boolean existsByBoardName(String boardName);
}
