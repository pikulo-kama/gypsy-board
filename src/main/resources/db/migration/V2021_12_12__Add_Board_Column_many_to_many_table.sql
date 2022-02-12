CREATE TABLE "board_column"
(
    "board_id"  BIGINT,
    "column_id" BIGINT
);

ALTER TABLE "board_column"
    ADD FOREIGN KEY ("board_id") REFERENCES "boards" ("board_id");

ALTER TABLE "board_column"
    ADD FOREIGN KEY ("column_id") REFERENCES "board_columns" ("column_id");