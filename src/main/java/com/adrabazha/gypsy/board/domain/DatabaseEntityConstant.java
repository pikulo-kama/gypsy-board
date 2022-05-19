package com.adrabazha.gypsy.board.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseEntityConstant {

    public static final String USER = "users";

    public static final String ORGANIZATION = "organizations";

    public static final String ORGANIZATION_ROLE = "user_roles";

    public static final String BOARD = "boards";

    public static final String BOARD_COLUMN = "board_columns";

    public static final String TASK = "tasks";

    public static final String REGISTRATION_TOKEN = "registration_tokens";

    public static final String MEMBERSHIP_TOKEN = "membership_tokens";

    public static final String ABSENCE_RECORD = "absence_history";

    public static final String MONGO_SEQUENCES = "database_sequences";

    public static final String DOCUMENT = "documents";
}
