CREATE TABLE user_permissions (
    "organization_id" BIGINT NOT NULL,
    "user_id" BIGINT NOT NULL,
    "role_id" BIGINT NOT NULL,
    UNIQUE ("organization_id", "user_id")
);

ALTER TABLE user_permissions ADD FOREIGN KEY ("organization_id")
    REFERENCES organizations ("organization_id") ON DELETE CASCADE;

ALTER TABLE user_permissions ADD FOREIGN KEY ("user_id")
    REFERENCES users ("user_id") ON DELETE CASCADE;

ALTER TABLE user_permissions ADD FOREIGN KEY ("role_id")
    REFERENCES user_roles ("role_id") ON DELETE CASCADE;