package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.OrganizationRole;
import com.adrabazha.gypsy.board.domain.Role;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.OrganizationForm;
import com.adrabazha.gypsy.board.dto.form.OrganizationMemberForm;
import com.adrabazha.gypsy.board.dto.form.OrganizationMembersForm;
import com.adrabazha.gypsy.board.dto.form.UpdateMemberRoleForm;
import com.adrabazha.gypsy.board.dto.response.OrganizationReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.OrganizationResponse;
import com.adrabazha.gypsy.board.dto.response.UserReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.UserResponse;
import com.adrabazha.gypsy.board.event.NewMembersAddedEvent;
import com.adrabazha.gypsy.board.exception.GeneralException;
import com.adrabazha.gypsy.board.exception.UserMessageException;
import com.adrabazha.gypsy.board.mapper.OrganizationMapper;
import com.adrabazha.gypsy.board.mapper.UserMapper;
import com.adrabazha.gypsy.board.repository.OrganizationRepository;
import com.adrabazha.gypsy.board.repository.OrganizationRoleRepository;
import com.adrabazha.gypsy.board.utils.resolver.OrganizationHashResolver;
import com.adrabazha.gypsy.board.utils.resolver.UserHashResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationRoleRepository organizationRoleRepository;
    private final OrganizationMapper organizationMapper;
    private final OrganizationHashResolver organizationHashResolver;
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserHashResolver userHashResolver;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository organizationRepository,
                                   OrganizationRoleRepository organizationRoleRepository,
                                   OrganizationMapper organizationMapper,
                                   OrganizationHashResolver organizationHashResolver,
                                   UserService userService,
                                   UserMapper userMapper,
                                   UserHashResolver userHashResolver,
                                   ApplicationEventPublisher eventPublisher) {
        this.organizationRepository = organizationRepository;
        this.organizationRoleRepository = organizationRoleRepository;
        this.organizationMapper = organizationMapper;
        this.organizationHashResolver = organizationHashResolver;
        this.userService = userService;
        this.userMapper = userMapper;
        this.userHashResolver = userHashResolver;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Organization findById(Long organizationId) {
        return organizationRepository.findById(organizationId)
                .orElseThrow(() -> new GeneralException("Organization not found"));
    }

    @Override
    public List<OrganizationRole> getOrganizationRoles() {
        return organizationRoleRepository.findAll();
    }

    @Override
    @Transactional
    public UserMessage updateMemberRole(UpdateMemberRoleForm form, Long organizationId) {
        Long userId = userHashResolver.retrieveIdentifier(form.getUserHash());
        String userCurrentRole = organizationRepository.getOrganizationMemberRole(userId, organizationId);
        UserMessage userMessage;

        if (isLastAdminInOrganization(organizationId, userCurrentRole)) {
            userMessage = UserMessage.error("Can't remove last admin from organization");
        } else {

            OrganizationRole newUserRole = organizationRoleRepository.findByRoleCode(form.getRoleCode());
            organizationRepository.updateUserRoleInOrganization(userId, organizationId, newUserRole.getRoleId());
            userCurrentRole = newUserRole.getRoleCode();

            userMessage = UserMessage.success("Successfully changed role");
        }

        userMessage.addResponseDataEntry("newRole", userCurrentRole);
        return userMessage;
    }

    @Transactional
    @Override
    public UserMessage createOrganization(OrganizationForm form, User currentUser, HttpServletRequest request) {

        if (organizationRepository.existsByOrganizationName(form.getOrganizationName())) {
            throw new UserMessageException("Organization name already exist");
        }

        Organization organization = Organization.builder()
                .organizationName(form.getOrganizationName())
                .build();
        Organization persistedOrganization = organizationRepository.save(organization);

        OrganizationRole adminRole = organizationRoleRepository.findByRoleCode(Role.ADMIN.getRoleName());
        OrganizationRole standardRole = organizationRoleRepository.findByRoleCode(Role.STANDARD.getRoleName());

        List<String> memberUsernames = form.getMemberUsernames();
        List<User> members = userService.findUsersByUsernames(memberUsernames);

        this.addUsersToOrganization(Collections.singletonList(currentUser), persistedOrganization, adminRole.getRoleId(), true);
        this.addUsersToOrganization(members, persistedOrganization, standardRole.getRoleId(), false);

        triggerNewMembersAddedEvent(request, persistedOrganization, members);

        UserMessage userMessage = UserMessage.success("Organizations was created");
        userMessage.addResponseDataEntry("persistedOrganization",
                organizationMapper.mapOrganizationsToReferenceResponse(persistedOrganization));
        return userMessage;
    }

    @Override
    @Transactional
    public UserMessage addMembersToOrganization(OrganizationMembersForm form, Long organizationId, HttpServletRequest request) {
        Organization organization = findById(organizationId);
        List<User> newMembers = userService.findUsersByUsernames(form.getOrganizationMembers());
        OrganizationRole defaultRole = organizationRoleRepository.findByRoleCode(Role.STANDARD.getRoleName());

        this.addUsersToOrganization(newMembers, organization, defaultRole.getRoleId(), false);

        triggerNewMembersAddedEvent(request, organization, newMembers);

        List<UserReferenceResponse> memberReferences = newMembers.stream()
                .map(userMapper::mapUserToReferenceResponse)
                .peek(reference -> {
                    reference.setActiveRole(defaultRole.getRoleCode());
                    reference.setIsInvitationAccepted(false);
                })
                .collect(Collectors.toList());

        UserMessage userMessage = UserMessage.success("New members was added");
        userMessage.addResponseDataEntry("organizationRoles", getOrganizationRoles());
        userMessage.addResponseDataEntry("organizationMembers", memberReferences);

        return userMessage;
    }

    @Override
    @Transactional
    public UserMessage removeOrganizationMember(OrganizationMemberForm form, Long organizationId) {
        Long userId = userHashResolver.retrieveIdentifier(form.getMemberHash());
        UserMessage message;

        String memberRole = getOrganizationMemberRole(userId, organizationId);
            if (isLastAdminInOrganization(organizationId, memberRole)) {
            message = UserMessage.error("Can't remove last admin in organization");
        } else {
            User memberToRemove = userService.findById(userId);
            organizationRepository.deleteConcreteUserFromOrganization(organizationId, memberToRemove.getUserId());

            message = UserMessage.success(String.format("Member '%s' was removed from organization", memberToRemove.getFullName()));
            message.addResponseDataEntry("removedMemberHash", form.getMemberHash());
        }

        return message;
    }

    @Override
    @Transactional
    public UserMessage deleteOrganization(String organizationHash) {
        Long organizationId = organizationHashResolver.retrieveIdentifier(organizationHash);
        organizationRepository.deleteUsersFromOrganization(organizationId);
        organizationRepository.deleteById(organizationId);
        return UserMessage.success("Organization was deleted");
    }

    @Override
    public Boolean isUserInOrganization(User user, Organization organization) {
        List<String> organizationUsers = organization.getUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        return organizationUsers.contains(user.getUsername());
    }

    @Override
    public List<OrganizationReferenceResponse> getOrganizationsByUser(Long userId) {
        User user = userService.findById(userId);
        return user.getOrganizations().stream()
                .filter(organization -> organizationRepository.isInvitationAccepted(user.getUserId(), organization.getOrganizationId()))
                .map(organizationMapper::mapOrganizationsToReferenceResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrganizationResponse getOrganizationResponseDto(Long organizationId, User currentUser) {
        Organization organization = findById(organizationId);

        if (!isUserInOrganization(currentUser, organization)) {
            throw new UserMessageException("You have no access to this organization");
        }

        OrganizationResponse organizationResponse = organizationMapper.mapOrganizationToResponse(organization);
        List<UserReferenceResponse> members = organization.getUsers().stream().map(user ->
                UserReferenceResponse.builder()
                        .userHash(userHashResolver.obtainHash(user.getUserId()))
                        .fullName(user.getFullName())
                        .activeRole(getOrganizationMemberRole(user.getUserId(), organizationId))
                        .isInvitationAccepted(organizationRepository.isInvitationAccepted(user.getUserId(), organizationId))
                        .build()
        ).collect(Collectors.toList());

        organizationResponse.setOrganizationMembers(members);

        return organizationResponse;
    }

    @Override
    public List<UserResponse> getOrganizationMembers(Long organizationId) {
        Organization organization = findById(organizationId);
        List<User> members = userService.findUsersFromOrganization(organization);
        return members.stream()
                .map(userMapper::mapUserToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public String getOrganizationMemberRole(Long userId, Long organizationId) {
        return organizationRepository.getOrganizationMemberRole(userId, organizationId);
    }

    @Override
    public UserMessage getMemberBlockedActionsSelector(Long userId, Long organizationId) {
        List<String> blockedObjects = organizationRepository.getMemberBlockedObjects(userId, organizationId);
        String selector = StringUtils.join(blockedObjects, ", ");

        UserMessage userMessage = UserMessage.success("Selector retrieved");
        userMessage.addResponseDataEntry("selector", selector);
        return userMessage;
    }

    private Boolean isLastAdminInOrganization(Long organizationId, String userCurrentRole) {
        return Objects.equals(Role.fromRoleName(userCurrentRole), Role.ADMIN) && organizationRepository.getAdminCountInOrganization(organizationId) == 1;
    }

    private void triggerNewMembersAddedEvent(HttpServletRequest request, Organization organization, List<User> members) {
        NewMembersAddedEvent event = new NewMembersAddedEvent(this, members, organization, request);
        eventPublisher.publishEvent(event);
    }

    private void addUsersToOrganization(List<User> users, Organization organization, Long roleId, Boolean isInvitationAccepted) {
        users.forEach(user -> organizationRepository
                .addUserToOrganization(user.getUserId(), organization.getOrganizationId(), roleId, isInvitationAccepted));
    }
}
