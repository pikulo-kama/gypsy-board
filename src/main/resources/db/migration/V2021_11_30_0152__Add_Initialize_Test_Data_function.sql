CREATE OR REPLACE FUNCTION initialize_test_data() returns void as $body$
DECLARE
    organizationId bigint;
    boardId        bigint;
    userId         bigint;
    columnId       bigint;
BEGIN
    INSERT INTO "organizations" (organization_name)
    VALUES ('Top Organization');

    SELECT organization_id
    INTO organizationId
    FROM "organizations"
    WHERE organization_name = 'Top Organization';

    -- Credentials: admin/admin
    INSERT INTO "users" (username, full_name, "password")
    VALUES ('admin', 'ADMIN', '$2a$10$lTP2KTWeIiaqUXm6Hlw5Hex7bz.NJpSBZ43Yb8764qbqNCFA9v1M2');

    SELECT user_id INTO userId FROM "users" WHERE username = 'admin';

    INSERT INTO "user_organization" (user_id, organization_id) VALUES (userId, organizationId);

    INSERT INTO "boards" (board_name, organization_id)
    VALUES ('Test Board', organizationId);

    SELECT board_id INTO boardId FROM "boards" WHERE board_name = 'Test Board';

    INSERT INTO "board_columns" ("column_name", column_order, board_id)
    VALUES ('Primary', 10, boardId),
           ('Secondary', 20, boardId);

    SELECT column_id INTO columnId FROM "board_columns" WHERE "column_name" = 'Primary';

    INSERT INTO "tasks" (task_name, task_order, column_id, user_assigned_id, task_description)
    VALUES ('Primary Task #1', 10, columnId, userId, 'text'),
           ('Primary Task #2', 20, columnId, userId, 'text'),
           ('Primary Task #3', 30, columnId, userId, 'text');

    SELECT column_id INTO columnId FROM "board_columns" WHERE "column_name" = 'Secondary';

    INSERT INTO "tasks" (task_name, task_order, column_id, user_assigned_id, task_description)
    VALUES ('Secondary Task #1', 10, columnId, userId, 'text'),
           ('Secondary Task #2', 20, columnId, userId, 'text'),
           ('Secondary Task #3', 30, columnId, userId, 'text');
END ;
$body$
LANGUAGE PLPGSQL
SECURITY DEFINER;
