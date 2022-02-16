CREATE TABLE "ui_actions"
(
    "action_id"       BIGSERIAL PRIMARY KEY,
    "action_name"     varchar(40) UNIQUE,
    "object_selector" varchar(50)
);

CREATE TABLE "role_blocked_actions"
(
    "role_id"   BIGINT NOT NULL,
    "action_id" BIGINT NOT NULL,
    UNIQUE ("role_id", "action_id")
);

ALTER TABLE "role_blocked_actions"
    ADD FOREIGN KEY ("role_id") REFERENCES "user_roles" ("role_id") ON DELETE CASCADE;

ALTER TABLE "role_blocked_actions"
    ADD FOREIGN KEY ("action_id") REFERENCES "ui_actions" ("action_id") ON DELETE CASCADE;


INSERT INTO "ui_actions" (action_name, object_selector)
VALUES ('Create board', '#create-board-btn'),
       ('Delete board', '#remove-board-btn'),
       ('Delete organization', '#remove-organization-btn'),
       ('Delete board column', '.remove-board-column-btn'),
       ('Create board column', '#add-column-btn');

INSERT INTO "role_blocked_actions" (role_id, action_id)
SELECT a.role_id, b.action_id
FROM user_roles a
         JOIN ui_actions b ON 1 = 1
WHERE a.role_id = (SELECT c.role_id FROM user_roles c WHERE c.role_code = 'assistant')
  AND b.action_id IN (SELECT action_id FROM ui_actions WHERE b.action_name IN ('Delete organization'));

INSERT INTO "role_blocked_actions" (role_id, action_id)
SELECT a.role_id, b.action_id
FROM user_roles a
         JOIN ui_actions b ON 1 = 1
WHERE a.role_id = (SELECT c.role_id FROM user_roles c WHERE c.role_code = 'standard')
  AND b.action_id IN (SELECT action_id FROM ui_actions WHERE b.action_name IN ('Delete organization',
                                                                               'Delete board',
                                                                               'Delete board column',
                                                                               'Create board column',
                                                                               'Create board'));
