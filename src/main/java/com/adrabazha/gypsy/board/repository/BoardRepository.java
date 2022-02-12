package com.adrabazha.gypsy.board.repository;

import com.adrabazha.gypsy.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Boolean existsByBoardName(String boardName);

    @Modifying
    @Query(value = "INSERT INTO BOARD_COLUMN (board_id, column_id) " +
            "VALUES (:boardId, :columnId)", nativeQuery = true)
    void addColumnToBoard(@Param("boardId") Long boardId,
                          @Param("columnId") Long columnId);
}
