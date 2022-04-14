package com.adrabazha.gypsy.board.mapper;

import com.adrabazha.gypsy.board.domain.User;
import com.adrabazha.gypsy.board.dto.response.UserReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.UserResponse;
import com.adrabazha.gypsy.board.utils.resolver.UserHashResolver;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final UserHashResolver userHashResolver;

    public UserMapper(UserHashResolver userHashResolver) {
        this.userHashResolver = userHashResolver;
    }

    public UserResponse mapUserToResponse(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .build();
    }

    public UserReferenceResponse mapUserToReferenceResponse(User user) {
        return UserReferenceResponse.builder()
                .fullName(user.getFullName())
                .userHash(userHashResolver.obtainHash(user.getUserId()))
                .build();
    }
}
