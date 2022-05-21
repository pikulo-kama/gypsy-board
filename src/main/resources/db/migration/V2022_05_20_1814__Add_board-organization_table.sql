CREATE TABLE "shared_boards"
(
    "board_id"        BIGINT  NOT NULL,
    "organization_id" BIGINT  NOT NULL
);

ALTER TABLE "shared_boards"
    ADD FOREIGN KEY ("board_id") REFERENCES "boards" ("board_id") ON DELETE CASCADE;

ALTER TABLE "shared_boards"
    ADD FOREIGN KEY ("organization_id") REFERENCES "organizations" ("organization_id") ON DELETE CASCADE;