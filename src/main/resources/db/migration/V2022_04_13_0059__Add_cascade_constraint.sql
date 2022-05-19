-- Add cascade delete for these columns

ALTER TABLE user_organization
DROP CONSTRAINT user_organization_organization_id_fkey,
    ADD CONSTRAINT user_organization_organization_id_fkey
        FOREIGN KEY (organization_id)
        REFERENCES organizations(organization_id)
            ON DELETE CASCADE;

ALTER TABLE user_organization
    DROP CONSTRAINT user_organization_user_id_fkey,
    ADD CONSTRAINT user_organization_user_id_fkey
        FOREIGN KEY (user_id)
            REFERENCES users(user_id)
            ON DELETE CASCADE;