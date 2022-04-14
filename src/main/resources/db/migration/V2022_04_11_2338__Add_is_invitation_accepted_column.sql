ALTER TABLE user_organization
    ADD COLUMN "is_invitation_accepted" BOOLEAN DEFAULT true;


CREATE TABLE IF NOT EXISTS membership_tokens
(
    "membership_token_id" BIGSERIAL PRIMARY KEY,
    "token"               VARCHAR UNIQUE NOT NULL,
    "expiry_date"         DATE           NOT NULL,
    "user_id"             BIGINT         NOT NULL,
    "organization_id"     BIGINT         NOT NULL
);

ALTER TABLE membership_tokens
    ADD FOREIGN KEY ("user_id") REFERENCES users ("user_id") ON DELETE CASCADE;

ALTER TABLE membership_tokens
    ADD FOREIGN KEY ("organization_id") REFERENCES organizations ("organization_id") ON DELETE CASCADE;