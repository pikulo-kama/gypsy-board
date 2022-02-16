package com.adrabazha.gypsy.board.repository;

import com.adrabazha.gypsy.board.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Boolean existsByOrganizationName(@NotNull String organizationName);

    @Modifying
    @Query(value = "INSERT INTO USER_ORGANIZATION (user_id, organization_id, role_id) " +
                    "VALUES (:userId, :organizationId, :roleId)", nativeQuery = true)
    void addUserToOrganization(@Param("userId") Long userId,
                               @Param("organizationId") Long organizationId,
                               @Param("roleId") Long roleId);

    @Modifying
    @Query(value = "DELETE FROM USER_ORGANIZATION " +
                    "WHERE organization_id = :organizationId", nativeQuery = true)
    void deleteUsersFromOrganization(@Param("organizationId") Long organizationId);

    @Query(value = "SELECT role_code FROM user_roles WHERE role_id = (SELECT role_id " +
            "FROM user_organization WHERE user_id = :userId AND organization_id = :orgId)", nativeQuery = true)
    String getOrganizationMemberRole(@Param("userId") Long userId, @Param("orgId") Long organizationId);

    @Query(value = "SELECT ua.object_selector " +
            "from role_blocked_actions a " +
            "         RIGHT JOIN ui_actions ua ON a.action_id = ua.action_id " +
            "where role_id = (SELECT role_id " +
            "                    FROM user_organization " +
            "                    WHERE user_id = :userId " +
            "                      AND organization_id = :orgId)", nativeQuery = true)
    List<String> getMemberBlockedObjects(@Param("userId") Long userId, @Param("orgId") Long organizationId);
}
