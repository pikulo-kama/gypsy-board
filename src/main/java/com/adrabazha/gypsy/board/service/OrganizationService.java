package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.Organization;
import com.adrabazha.gypsy.board.domain.User;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.OrganizationForm;
import com.adrabazha.gypsy.board.dto.response.OrganizationReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.OrganizationResponse;
import com.adrabazha.gypsy.board.dto.response.UserResponse;

import java.util.List;

public interface OrganizationService {

    Organization findById(Long organizationId);

    UserMessage createOrganization(OrganizationForm organizationForm, User currentUser);

    UserMessage deleteOrganization(String organizationHash, User currentUser);

    Boolean isUserInOrganization(User user, Organization organization);

    List<OrganizationReferenceResponse> getAvailableOrganizations(Long userId);

    OrganizationResponse getOrganizationResponseDto(Long organizationId, User currentUser);

    List<UserResponse> getOrganizationMembers(Long organizationId);

    String getOrganizationMemberRole(Long userId, Long organizationId);

    UserMessage getMemberBlockedActionsSelector(Long userId, Long organizationId);
}
