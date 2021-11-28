package com.adrabazha.gipsy.board.repository;

import com.adrabazha.gipsy.board.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    List<Organization> findOrganizationsByOrganizationNameContains(String organizationName);


}
