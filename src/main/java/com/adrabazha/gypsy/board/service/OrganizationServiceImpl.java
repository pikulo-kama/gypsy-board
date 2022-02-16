package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.Organization;
import com.adrabazha.gypsy.board.domain.OrganizationRole;
import com.adrabazha.gypsy.board.domain.User;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.OrganizationForm;
import com.adrabazha.gypsy.board.dto.response.OrganizationReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.OrganizationResponse;
import com.adrabazha.gypsy.board.dto.response.UserResponse;
import com.adrabazha.gypsy.board.exception.GeneralException;
import com.adrabazha.gypsy.board.exception.UserMessageException;
import com.adrabazha.gypsy.board.mapper.OrganizationMapper;
import com.adrabazha.gypsy.board.mapper.UserMapper;
import com.adrabazha.gypsy.board.repository.OrganizationRepository;
import com.adrabazha.gypsy.board.repository.OrganizationRoleRepository;
import com.adrabazha.gypsy.board.utils.resolver.OrganizationHashResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationRoleRepository organizationRoleRepository;
    private final OrganizationMapper organizationMapper;
    private final OrganizationHashResolver organizationHashResolver;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ResourceService resourceService;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository organizationRepository,
                                   OrganizationRoleRepository organizationRoleRepository, OrganizationMapper organizationMapper,
                                   OrganizationHashResolver organizationHashResolver,
                                   UserService userService, UserMapper userMapper, ResourceService resourceService) {
        this.organizationRepository = organizationRepository;
        this.organizationRoleRepository = organizationRoleRepository;
        this.organizationMapper = organizationMapper;
        this.organizationHashResolver = organizationHashResolver;
        this.userService = userService;
        this.userMapper = userMapper;
        this.resourceService = resourceService;
    }

    @Override
    public Organization findById(Long organizationId) {
        return organizationRepository.findById(organizationId)
                .orElseThrow(() -> new GeneralException("Organization not found"));
    }

    @Transactional
    @Override
    public UserMessage createOrganization(OrganizationForm form, User currentUser) {

        if (organizationRepository.existsByOrganizationName(form.getOrganizationName())) {
            throw new UserMessageException("Organization name already exist");
        }

        Organization organization = Organization.builder()
                .organizationName(form.getOrganizationName())
                .build();
        Organization persistedOrganization = organizationRepository.save(organization);

        OrganizationRole adminRole = organizationRoleRepository.findByRoleCode("administrator");
        OrganizationRole standardRole = organizationRoleRepository.findByRoleCode("standard");

        List<String> memberUsernames = form.getMemberUsernames();
        List<User> members = userService.findUsersByUsernames(memberUsernames);

        this.addUsersToOrganization(Collections.singletonList(currentUser), persistedOrganization, adminRole.getRoleId());
        this.addUsersToOrganization(members, persistedOrganization, standardRole.getRoleId());

        UserMessage userMessage = UserMessage.success("Organizations was created");
        userMessage.addResponseDataEntry("persistedOrganization",
                organizationMapper.mapOrganizationsToReferenceResponse(persistedOrganization));
        return userMessage;
    }

    @Override
    @Transactional
    public UserMessage deleteOrganization(String organizationHash, User currentUser) {
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
    public List<OrganizationReferenceResponse> getAvailableOrganizations(Long userId) {
        User user = userService.findById(userId);
        return user.getOrganizations().stream()
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
        organizationResponse.getOrganizationMembers()
                .forEach(member -> member.setImageName(resourceService.getRandomImage()));

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

    private void addUsersToOrganization(List<User> users, Organization organization, Long roleId) {
        users.forEach(user -> organizationRepository
                .addUserToOrganization(user.getUserId(), organization.getOrganizationId(), roleId));
    }
}
