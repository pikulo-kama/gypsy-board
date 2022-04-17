CREATE TABLE absence_history
(
    "absence_id"      BIGSERIAL NOT NULL,
    "member_id"       BIGINT    NOT NULL,
    "organization_id" BIGINT    NOT NULL,
    "absence_type"    VARCHAR   NOT NULL,
    "start_date"      DATE      NOT NULL,
    "end_date"        DATE      NOT NULL,
    "is_cancelled"    BOOLEAN   NOT NULL,
    "is_approved"     BOOLEAN   NOT NULL
);

ALTER TABLE absence_history
    ADD FOREIGN KEY ("organization_id")
        REFERENCES organizations ("organization_id") ON DELETE CASCADE;

ALTER TABLE absence_history
    ADD FOREIGN KEY ("member_id")
        REFERENCES users ("user_id") ON DELETE CASCADE;


ALTER TABLE membership_tokens ALTER COLUMN "expiry_date" TYPE TIMESTAMP;