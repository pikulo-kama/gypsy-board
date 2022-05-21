package com.adrabazha.gypsy.board.utils.resolver;

import com.adrabazha.gypsy.board.exception.GeneralException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum Resolver {

    ABSENCE_RECORD("ar"),
    BOARD_COLUMN("bc"),
    BOARD("b"),
    DOCUMENT("d"),
    TASK("t"),
    USER("u"),
    ORGANIZATION("o");

    @Getter
    private String prefix;

    public static Resolver fromHash(String hash) {
        String prefix = hash.substring(0, hash.indexOf("-"));
        return Arrays.stream(Resolver.values())
                .filter(resolver -> resolver.getPrefix().equals(prefix))
                .findFirst()
                .orElseThrow(() -> new GeneralException(
                        String.format("Resolver for prefix '%s' not found", prefix)));
    }
}
