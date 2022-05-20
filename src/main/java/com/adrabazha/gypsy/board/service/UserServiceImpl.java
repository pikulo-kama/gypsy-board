package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.form.RegisterForm;
import com.adrabazha.gypsy.board.dto.response.UserResponse;
import com.adrabazha.gypsy.board.exception.GeneralException;
import com.adrabazha.gypsy.board.exception.UserMessageException;
import com.adrabazha.gypsy.board.utils.mapper.UserMapper;
import com.adrabazha.gypsy.board.repository.OrganizationRepository;
import com.adrabazha.gypsy.board.repository.UserRepository;
import com.adrabazha.gypsy.board.utils.mail.CustomEventPublisher;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService, AuthenticationService {

    private final CustomEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final OrganizationRepository organizationRepository;

    @Autowired
    public UserServiceImpl(CustomEventPublisher eventPublisher,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper,
                           OrganizationRepository organizationRepository) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException("User not found"));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUsers(List<User> users) {
        userRepository.deleteAll(users);
    }

    @Override
    public List<UserResponse> lookupByInputString(String inputString) {
        List<UserResponse> users = Collections.emptyList();

        if (!inputString.isEmpty()) {
            Pageable userLimit = PageRequest.of(0, 5);
            users = userRepository.findUsersByUsernameContainsOrFullNameContains(inputString, inputString, userLimit)
                    .stream()
                    .filter(User::getIsEnabled)
                    .map(userMapper::mapUserToResponse)
                    .collect(Collectors.toList());
        }
        return users;
    }

    @Override
    public List<User> findUsersByUsernames(List<String> usernames) {
        return userRepository.findUsersByUsernameIn(usernames);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new GeneralException("User not found"));
    }

    @Override
    public List<User> findUsersFromOrganization(Organization organization) {
        return userRepository.findUsersByOrganizationsContains(organization);
    }

    @Override
    @Transactional
    public UserMessage findOrganizationMembersByInput(String input, Long organizationId) {
        List<UserResponse> userResponses = lookupByInputString(input);

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new UserMessageException("Organization not found"));

        List<String> memberUsernames = findUsersFromOrganization(organization).stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        userResponses = userResponses.stream()
                .filter(userResponse -> !memberUsernames.contains(userResponse.getUsername()))
                .collect(Collectors.toList());

        UserMessage userMessage = UserMessage.success(String.format("%d users was found", userResponses.size()));
        userMessage.addResponseDataEntry("users", userResponses);

        return userMessage;
    }

    @Override
    public void register(RegisterForm form, HttpServletRequest request) {
        if (!StringUtils.equals(form.getPassword(), form.getPasswordRepeat())) {
            throw new UserMessageException("Passwords don't match");
        }
        if (userExistsByUsername(form.getUsername())) {
            throw new UserMessageException("User with this username already registered");
        }
        if (userExistsByEmail(form.getEmail())) {
            throw new UserMessageException("User with this email already registered");
        }

        User user = User.builder()
                .email(form.getEmail())
                .username(form.getUsername())
                .fullName(String.format("%s %s", form.getFirstName(), form.getLastName()))
                .password(passwordEncoder.encode(form.getPassword()))
                .isEnabled(false)
                .build();

        User persistedUser = userRepository.save(user);

        eventPublisher.publishRegistrationCompletedEvent(this, persistedUser, request);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with such username doesn't exist"));
    }

    private Boolean userExistsByUsername(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }

    private Boolean userExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
