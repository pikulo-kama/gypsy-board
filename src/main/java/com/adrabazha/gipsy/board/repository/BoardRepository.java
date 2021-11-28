package com.adrabazha.gipsy.board.repository;

import com.adrabazha.gipsy.board.domain.Board;
import com.adrabazha.gipsy.board.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findBoardsByOrganizationAndBoardNameContains(Organization organization, String boardName);
}
