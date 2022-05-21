package com.adrabazha.gypsy.board.utils.mapper;

import com.adrabazha.gypsy.board.domain.sql.User;
import com.adrabazha.gypsy.board.dto.response.UserReferenceResponse;
import com.adrabazha.gypsy.board.dto.response.UserResponse;
import com.adrabazha.gypsy.board.utils.resolver.HashResolverFactory;
import com.adrabazha.gypsy.board.utils.resolver.Resolver;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final HashResolverFactory hashResolverFactory;

    public UserMapper(HashResolverFactory hashResolverFactory) {
        this.hashResolverFactory = hashResolverFactory;
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
                .userHash(hashResolverFactory.obtainHash(user.getUserId(), Resolver.USER))
                .build();
    }
}
