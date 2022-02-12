package com.adrabazha.gypsy.board.repository;

import com.adrabazha.gypsy.board.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Boolean existsByOrganizationName(@NotNull String organizationName);

    @Modifying
    @Query(value = "INSERT INTO USER_ORGANIZATION (user_id, organization_id) " +
                    "VALUES (:userId, :organizationId)", nativeQuery = true)
    void addUserToOrganization(@Param("userId") Long userId,
                               @Param("organizationId") Long organizationId);

    @Modifying
    @Query(value = "DELETE FROM USER_ORGANIZATION " +
                    "WHERE organization_id = :organizationId", nativeQuery = true)
    void deleteUsersFromOrganization(@Param("organizationId") Long organizationId);
}
