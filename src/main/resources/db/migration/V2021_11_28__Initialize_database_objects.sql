CREATE TABLE "users"
(
    "user_id"   BIGSERIAL PRIMARY KEY,
    "username"  VARCHAR(20) UNIQUE NOT NULL,
    "full_name" VARCHAR(50)        NOT NULL,
    "password"  VARCHAR(60)        NOT NULL
);

CREATE TABLE "organizations"
(
    "organization_id"   BIGSERIAL PRIMARY KEY,
    "organization_name" VARCHAR(30) UNIQUE NOT NULL
);

CREATE TABLE "boards"
(
    "board_id"        BIGSERIAL PRIMARY KEY,
    "board_name"      VARCHAR(30) UNIQUE NOT NULL,
    "organization_id" BIGINT
);

CREATE TABLE "user_roles"
(
    "role_id"   BIGSERIAL PRIMARY KEY,
    "role_code" VARCHAR(15) NOT NULL,
    "role_name" VARCHAR(70)
);

CREATE TABLE "user_organization"
(
    "user_id"         BIGINT NOT NULL,
    "organization_id" BIGINT NOT NULL,
    role_id           BIGINT NOT NULL
);

CREATE TABLE "board_columns"
(
    "column_id"    BIGSERIAL PRIMARY KEY,
    "column_name"  VARCHAR(30) NOT NULL,
    "column_order" INTEGER     NOT NULL,
    "board_id"     BIGINT,
    UNIQUE ("board_id", "column_name")
);

CREATE TABLE "tasks"
(
    "task_id"          BIGSERIAL PRIMARY KEY,
    "task_name"        VARCHAR(30) NOT NULL,
    "task_order"       INTEGER     NOT NULL,
    "column_id"        BIGINT,
    "user_assigned_id" BIGINT,
    "task_description" VARCHAR
);

ALTER TABLE "boards"
    ADD FOREIGN KEY ("organization_id") REFERENCES "organizations" ("organization_id") ON DELETE CASCADE;

ALTER TABLE "board_columns"
    ADD FOREIGN KEY ("board_id") REFERENCES "boards" ("board_id") ON DELETE CASCADE;

ALTER TABLE "user_organization"
    ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id");

ALTER TABLE "user_organization"
    ADD FOREIGN KEY ("organization_id") REFERENCES "organizations" ("organization_id");

ALTER TABLE "user_organization"
    ADD FOREIGN KEY ("role_id") REFERENCES "user_roles" ("role_id") ON DELETE CASCADE;

ALTER TABLE "tasks"
    ADD FOREIGN KEY ("column_id") REFERENCES "board_columns" ("column_id") ON DELETE CASCADE;

ALTER TABLE "tasks"
    ADD FOREIGN KEY ("user_assigned_id") REFERENCES "users" ("user_id") ON DELETE SET NULL;
