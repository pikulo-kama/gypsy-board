CREATE TABLE "users"
(
    "user_id"   BIGINT PRIMARY KEY,
    "username"  VARCHAR,
    "full_name" VARCHAR,
    "password"  VARCHAR
);

CREATE TABLE "organizations"
(
    "organization_id"   BIGINT PRIMARY KEY,
    "organization_name" VARCHAR
);

CREATE TABLE "boards"
(
    "board_id"        BIGINT PRIMARY KEY,
    "board_name"      VARCHAR,
    "organization_id" BIGINT
);

CREATE TABLE "user_organization"
(
    "user_id"         BIGINT,
    "organization_id" BIGINT
);

CREATE TABLE "board_columns"
(
    "column_id"    BIGINT PRIMARY KEY,
    "column_name"  VARCHAR,
    "column_order" INTEGER
);

CREATE TABLE "tasks"
(
    "task_id"          BIGINT PRIMARY KEY,
    "task_name"        VARCHAR,
    "task_order"       INTEGER,
    "column_id"        BIGINT,
    "user_assigned"    BIGINT,
    "task_description" VARCHAR
);

CREATE TABLE "comments"
(
    "comment_id"    BIGINT PRIMARY KEY,
    "task_id"       BIGINT,
    "author_id"     BIGINT,
    "creation_date" TIMESTAMP,
    "edited"        BOOLEAN
);

ALTER TABLE "boards"
    ADD FOREIGN KEY ("organization_id") REFERENCES "organizations" ("organization_id");

ALTER TABLE "user_organization"
    ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id");

ALTER TABLE "user_organization"
    ADD FOREIGN KEY ("organization_id") REFERENCES "organizations" ("organization_id");

ALTER TABLE "tasks"
    ADD FOREIGN KEY ("column_id") REFERENCES "board_columns" ("column_id");

ALTER TABLE "tasks"
    ADD FOREIGN KEY ("user_assigned") REFERENCES "users" ("user_id");

ALTER TABLE "comments"
    ADD FOREIGN KEY ("task_id") REFERENCES "tasks" ("task_id");

ALTER TABLE "comments"
    ADD FOREIGN KEY ("author_id") REFERENCES "users" ("user_id");
