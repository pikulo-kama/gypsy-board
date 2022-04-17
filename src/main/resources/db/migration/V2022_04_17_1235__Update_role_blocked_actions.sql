
CREATE FUNCTION func_add_role_blocked_action(p_role_code varchar, p_action_name varchar) RETURNS VOID AS $$
BEGIN
    INSERT INTO "role_blocked_actions" (role_id, action_id)
    SELECT a.role_id, b.action_id
    FROM user_roles a
             JOIN ui_actions b ON 1 = 1
    WHERE a.role_id = (SELECT c.role_id FROM user_roles c WHERE c.role_code = p_role_code)
      AND b.action_id IN (SELECT action_id FROM ui_actions WHERE b.action_name IN (p_action_name));
END;
$$ LANGUAGE plpgsql;

INSERT INTO "ui_actions" (action_name, object_selector)
VALUES ('Add organization members', '#add-members-btn'),
       ('Remove organization member button', '.remove-organization-member-btn'),
       ('Manage absences button', '.manage-absences-btn'),
       ('Change member role select box', '.organization-role-select');

select func_add_role_blocked_action('standard', 'Add organization members');
select func_add_role_blocked_action('standard', 'Remove organization member button');
select func_add_role_blocked_action('standard', 'Manage absences button');
select func_add_role_blocked_action('assistant', 'Manage absences button');
select func_add_role_blocked_action('standard', 'Change member role select box');
select func_add_role_blocked_action('assistant', 'Change member role select box');
