package com.adrabazha.gipsy.board.repository;

import com.adrabazha.gipsy.board.domain.Board;
import com.adrabazha.gipsy.board.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Board getBoardByOrganizationAndBoardNameContains(Organization organization, String boardName);
}
