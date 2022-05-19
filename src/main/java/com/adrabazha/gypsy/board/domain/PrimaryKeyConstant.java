package com.adrabazha.gypsy.board.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrimaryKeyConstant {

    public static final String USER = "user_id";

    public static final String BOARD = "board_id";

    public static final String BOARD_COLUMN = "column_id";

    public static final String ORGANIZATION = "organization_id";

    public static final String ORGANIZATION_ROLE = "role_id";

    public static final String TASK = "task_id";

    public static final String REGISTRATION_TOKEN = "registration_token_id";

    public static final String MEMBERSHIP_TOKEN = "membership_token_id";

    public static final String ABSENCE_RECORD = "absence_id";
}
