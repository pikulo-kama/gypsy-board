package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.Organization;
import com.adrabazha.gypsy.board.domain.User;
import com.adrabazha.gypsy.board.dto.form.RegisterForm;
import com.adrabazha.gypsy.board.dto.response.UserResponse;
import com.adrabazha.gypsy.board.event.RegistrationCompletedEvent;
import com.adrabazha.gypsy.board.exception.GeneralException;
import com.adrabazha.gypsy.board.exception.UserMessageException;
import com.adrabazha.gypsy.board.mapper.UserMapper;
import com.adrabazha.gypsy.board.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService, AuthenticationService {

    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(ApplicationEventPublisher eventPublisher, UserRepository userRepository,
                           PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
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
    public List<User> findUsersFromOrganization(Organization organization, String input) {
        return userRepository.findUsersByOrganizationsContainsAndUsernameContains(organization, input);
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

        RegistrationCompletedEvent event = RegistrationCompletedEvent.builder()
                .user(persistedUser)
                .request(request)
                .build();
        eventPublisher.publishEvent(event);
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
