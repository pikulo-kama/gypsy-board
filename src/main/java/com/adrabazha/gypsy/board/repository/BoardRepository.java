package com.adrabazha.gypsy.board.repository;

import com.adrabazha.gypsy.board.domain.sql.Board;
import com.adrabazha.gypsy.board.domain.sql.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Boolean existsByBoardNameAndOrganization(@NotNull String boardName, @NotNull Organization organization);

    @Query(value = "SELECT b.* " +
            "FROM boards b " +
            "         JOIN shared_boards st ON st.board_id = b.board_id " +
            "WHERE st.organization_id = :orgId", nativeQuery = true)
    List<Map<String, Object>> getOrganizationSharedBoards(@Param("orgId") Long organizationId);

    @Query(value = "SELECT o.organization_id " +
            "FROM organizations o " +
            "         JOIN shared_boards st ON st.organization_id = o.organization_id " +
            "WHERE st.board_id = :boardId", nativeQuery = true)
    List<Long> getCollaboratorsIdsByBoard(@Param("boardId") Long boardId);

    @Modifying
    @Query(value = "INSERT INTO shared_boards (board_id, organization_id) " +
            "VALUES (:boardId, :orgId)", nativeQuery = true)
    void shareBoard(@Param("boardId") Long boardId, @Param("orgId") Long organizationId);
}
