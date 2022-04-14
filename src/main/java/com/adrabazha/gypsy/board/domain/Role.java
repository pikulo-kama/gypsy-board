package com.adrabazha.gypsy.board.domain;

import com.adrabazha.gypsy.board.exception.GeneralException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum Role {

    ADMIN("administrator"),
    ASSISTANT("assistant"),
    STANDARD("standard");

    @Getter
    private String roleName;

    public static Role fromRoleName(String roleName) {
        return Arrays.stream(Role.values())
                .filter(role -> role.roleName.equals(roleName))
                .findFirst()
                .orElseThrow(() -> new GeneralException("Role not found"));

    }
}
