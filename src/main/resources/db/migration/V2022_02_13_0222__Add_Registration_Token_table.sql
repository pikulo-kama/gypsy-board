CREATE TABLE IF NOT EXISTS registration_tokens (
    "registration_token_id" BIGSERIAL PRIMARY KEY,
    "token" VARCHAR UNIQUE NOT NULL,
    "expiry_date" DATE NOT NULL,
    "user_id" BIGINT NOT NULL
);

ALTER TABLE registration_tokens
    ADD FOREIGN KEY ("user_id") REFERENCES users ("user_id") ON DELETE CASCADE;