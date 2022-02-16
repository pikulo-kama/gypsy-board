package com.adrabazha.gypsy.board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Role {

    ADMIN("administrator"),
    ASSISTANT("assistant"),
    STANDARD("standard");

    @Getter
    private String roleName;
}
