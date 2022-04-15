package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.OrganizationRole;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.OrganizationForm;
import com.adrabazha.gypsy.board.dto.form.OrganizationMemberForm;
import com.adrabazha.gypsy.board.dto.form.OrganizationMembersForm;
import com.adrabazha.gypsy.board.dto.form.UpdateMemberRoleForm;
import com.adrabazha.gypsy.board.dto.response.OrganizationReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.OrganizationResponse;
import com.adrabazha.gypsy.board.dto.response.UserResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrganizationService {

    Organization findById(Long organizationId);

    UserMessage createOrganization(OrganizationForm organizationForm, User currentUser, HttpServletRequest request);

    UserMessage deleteOrganization(String organizationHash);

    Boolean isUserInOrganization(User user, Organization organization);

    List<OrganizationReferenceResponse> getOrganizationsByUser(Long userId);

    OrganizationResponse getOrganizationResponseDto(Long organizationId, User currentUser);

    List<UserResponse> getOrganizationMembers(Long organizationId);

    String getOrganizationMemberRole(Long userId, Long organizationId);

    UserMessage getMemberBlockedActionsSelector(Long userId, Long organizationId);

    List<OrganizationRole> getOrganizationRoles();

    UserMessage updateMemberRole(UpdateMemberRoleForm form, Long organizationId);

    UserMessage addMembersToOrganization(OrganizationMembersForm form, Long organizationIdm, HttpServletRequest request);

    UserMessage removeOrganizationMember(OrganizationMemberForm form, Long organizationId);
}
