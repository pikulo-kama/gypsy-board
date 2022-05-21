package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.Role;
import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.OrganizationRole;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.OrganizationForm;
import com.adrabazha.gypsy.board.dto.form.OrganizationMemberForm;
import com.adrabazha.gypsy.board.dto.form.OrganizationMembersForm;
import com.adrabazha.gypsy.board.dto.form.UpdateMemberRoleForm;
import com.adrabazha.gypsy.board.dto.response.BoardReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.OrganizationReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.OrganizationResponse;
import com.adrabazha.gypsy.board.dto.response.UserReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.UserResponse;
import com.adrabazha.gypsy.board.exception.GeneralException;
import com.adrabazha.gypsy.board.exception.UserMessageException;
import com.adrabazha.gypsy.board.repository.BoardRepository;
import com.adrabazha.gypsy.board.utils.mapper.OrganizationMapper;
import com.adrabazha.gypsy.board.utils.mapper.UserMapper;
import com.adrabazha.gypsy.board.repository.OrganizationRepository;
import com.adrabazha.gypsy.board.repository.OrganizationRoleRepository;
import com.adrabazha.gypsy.board.utils.mail.CustomEventPublisher;
import com.adrabazha.gypsy.board.utils.mail.templates.MessageTemplates;
import com.adrabazha.gypsy.board.utils.resolver.HashResolverFactory;
import com.adrabazha.gypsy.board.utils.resolver.Resolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationRoleRepository organizationRoleRepository;
    private final OrganizationMapper organizationMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final CustomEventPublisher eventPublisher;
    private final HashResolverFactory hashResolverFactory;
    private final BoardRepository boardRepository;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository organizationRepository,
                                   OrganizationRoleRepository organizationRoleRepository,
                                   OrganizationMapper organizationMapper,
                                   UserService userService,
                                   UserMapper userMapper,
                                   CustomEventPublisher eventPublisher,
                                   HashResolverFactory hashResolverFactory,
                                   BoardRepository boardRepository) {
        this.organizationRepository = organizationRepository;
        this.organizationRoleRepository = organizationRoleRepository;
        this.organizationMapper = organizationMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.eventPublisher = eventPublisher;
        this.hashResolverFactory = hashResolverFactory;
        this.boardRepository = boardRepository;
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
    public UserMessage updateMemberRole(UpdateMemberRoleForm form, Long organizationId, User currentUser) {
        Long userId = hashResolverFactory.retrieveIdentifier(form.getUserHash());
        String userCurrentRole = organizationRepository.getOrganizationMemberRole(userId, organizationId);
        UserMessage userMessage;

        if (isLastAdminInOrganization(organizationId, userCurrentRole)) {
            userMessage = UserMessage.error("Can't remove last admin from organization");
        } else {

            OrganizationRole newUserRole = organizationRoleRepository.findByRoleCode(form.getRoleCode());
            organizationRepository.updateUserRoleInOrganization(userId, organizationId, newUserRole.getRoleId());
            userCurrentRole = newUserRole.getRoleCode();

            eventPublisher.publishMemberRelatedEvent(
                    this,
                    findById(organizationId),
                    userService.findById(userId),
                    currentUser,
                    MessageTemplates.memberRoleWasUpdated(newUserRole));

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

        triggerNewMembersAddedEvent(request, persistedOrganization, members, currentUser);

        UserMessage userMessage = UserMessage.success("Organizations was created");
        userMessage.addResponseDataEntry("persistedOrganization",
                organizationMapper.mapOrganizationsToReferenceResponse(persistedOrganization));
        return userMessage;
    }

    @Override
    @Transactional
    public UserMessage addMembersToOrganization(OrganizationMembersForm form, Long organizationId,
                                                HttpServletRequest request, User currentUser) {
        Organization organization = findById(organizationId);
        List<User> newMembers = userService.findUsersByUsernames(form.getOrganizationMembers());
        OrganizationRole defaultRole = organizationRoleRepository.findByRoleCode(Role.STANDARD.getRoleName());

        this.addUsersToOrganization(newMembers, organization, defaultRole.getRoleId(), false);

        triggerNewMembersAddedEvent(request, organization, newMembers, currentUser);

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
    public UserMessage removeOrganizationMember(OrganizationMemberForm form, Long organizationId, User currentUser) {
        Long userId = hashResolverFactory.retrieveIdentifier(form.getMemberHash());
        UserMessage message;

        String memberRole = getOrganizationMemberRole(userId, organizationId);
        if (isLastAdminInOrganization(organizationId, memberRole)) {
            message = UserMessage.error("Can't remove last admin in organization");
        } else {
            User memberToRemove = userService.findById(userId);
            organizationRepository.deleteConcreteUserFromOrganization(organizationId, memberToRemove.getUserId());
            Organization organization = findById(organizationId);

            eventPublisher.publishMemberRelatedEvent(this,
                    organization,
                    memberToRemove,
                    currentUser,
                    MessageTemplates.memberWasRemovedFromOrganization());

            eventPublisher.publishOrganizationRelatedEvent(this,
                    organization,
                    currentUser,
                    MessageTemplates.memberWasRemovedFromOrganization(memberToRemove));

            message = UserMessage.success(String.format("Member '%s' was removed from organization", memberToRemove.getFullName()));
            message.addResponseDataEntry("removedMemberHash", form.getMemberHash());
        }

        return message;
    }

    @Override
    public List<OrganizationReferenceResponse> getAllByInput(String input) {
        List<OrganizationReferenceResponse> organizations = Collections.emptyList();

        if (!input.isEmpty()) {
            Pageable limit = PageRequest.of(0, 5);
            organizations = organizationRepository.findAllByOrganizationNameContains(input, limit).stream()
                    .map(organizationMapper::mapOrganizationsToReferenceResponse)
                    .collect(Collectors.toList());
        }
        return organizations;
    }

    @Override
    @Transactional
    public UserMessage deleteOrganization(String organizationHash, User currentUser) {
        Long organizationId = hashResolverFactory.retrieveIdentifier(organizationHash);
        Organization organization = findById(organizationId);
        organizationRepository.deleteUsersFromOrganization(organization.getOrganizationId());
        organizationRepository.deleteById(organization.getOrganizationId());

        eventPublisher.publishOrganizationRelatedEvent(this, organization,
                currentUser, MessageTemplates.organizationDeleted());

        return UserMessage.success("Organization was deleted");
    }

    @Override
    public Boolean isUserInOrganization(User user, Organization organization) {
        List<String> organizationUsers = organization.getActiveMembers().stream()
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
        // Add members with their correct roles
        List<UserReferenceResponse> members = organization.getAllMembers().stream().map(user ->
                UserReferenceResponse.builder()
                        .userHash(hashResolverFactory.obtainHash(user.getUserId(), Resolver.USER))
                        .fullName(user.getFullName())
                        .activeRole(getOrganizationMemberRole(user.getUserId(), organizationId))
                        .isInvitationAccepted(organizationRepository.isInvitationAccepted(user.getUserId(), organizationId))
                        .build()
        ).collect(Collectors.toList());
        organizationResponse.setOrganizationMembers(members);

        // Add boards shared by other organizations
        List<BoardReferenceResponse> sharedBoards = getOrganizationSharedBoards(organization.getOrganizationId());
        organizationResponse.addOrganizationBoards(sharedBoards);

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

    private void triggerNewMembersAddedEvent(HttpServletRequest request, Organization organization, List<User> members, User memberPerformed) {
        eventPublisher.publishNewMembersAddedEvent(this, members, organization, request);
        members.forEach(member -> eventPublisher.publishOrganizationRelatedEvent(
                this,
                organization,
                memberPerformed,
                MessageTemplates.memberWasInvitedToOrganization(member)));
    }

    private List<BoardReferenceResponse> getOrganizationSharedBoards(Long organizationId) {
        List<Map<String, Object>> rawBoards = boardRepository.getOrganizationSharedBoards(organizationId);
        return rawBoards.stream().map(boardMap -> {
            Long boardId = ((BigInteger) boardMap.get("board_id")).longValue();
            String boardName = (String) boardMap.get("board_name");
            Long organizationOwnerId = ((BigInteger) boardMap.get("organization_id")).longValue();

            return BoardReferenceResponse.builder()
                    .boardHash(hashResolverFactory.obtainHash(boardId, Resolver.BOARD))
                    .boardName(boardName)
                    .isShared(true)
                    .ownerOrganization(findById(organizationOwnerId))
                    .build();
        }).collect(Collectors.toList());
    }

    private void addUsersToOrganization(List<User> users, Organization organization, Long roleId, Boolean isInvitationAccepted) {
        users.forEach(user -> organizationRepository
                .addUserToOrganization(user.getUserId(), organization.getOrganizationId(), roleId, isInvitationAccepted));
    }
}
