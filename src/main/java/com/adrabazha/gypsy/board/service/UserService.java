package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.UserMessage;
import com.adrabazha.gypsy.board.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    User findById(Long userId);

    User save(User user);

    void deleteUsers(List<User> users);

    List<UserResponse> lookupByInputString(String inputString);

    List<User> findUsersByUsernames(List<String> usernames);

    User findUserByUsername(String username);

    List<User> findUsersFromOrganization(Organization organization);

    UserMessage findOrganizationMembersByInput(String input, Long organizationId);
}
