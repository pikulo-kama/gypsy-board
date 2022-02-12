package com.adrabazha.gypsy.board.mapper;

import com.adrabazha.gypsy.board.domain.User;
import com.adrabazha.gypsy.board.dto.response.UserImageResponse;
import com.adrabazha.gypsy.board.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse mapUserToResponse(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .build();
    }

    public UserImageResponse mapUserToImageResponse(User user) {
        return UserImageResponse.builder()
                .fullName(user.getFullName())
                .imageName(null)
                .build();
    }
}
