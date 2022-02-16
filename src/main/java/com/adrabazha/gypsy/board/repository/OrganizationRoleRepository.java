package com.adrabazha.gypsy.board.repository;

import com.adrabazha.gypsy.board.domain.OrganizationRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRoleRepository extends JpaRepository<OrganizationRole, Long> {

    OrganizationRole findByRoleCode(String roleCode);
}
