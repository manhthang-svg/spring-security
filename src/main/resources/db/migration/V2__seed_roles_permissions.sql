
-- =============================================================================
-- SEED DATA: ROLES
-- =============================================================================
INSERT INTO roles(name)
VALUES
    ('ADMIN'),
    ('MANAGER'),
    ('USER');

-- =============================================================================
-- SEED DATA: PERMISSIONS
-- =============================================================================
INSERT INTO permissions(name, description)
VALUES
    ('USER_READ', 'Read users'),
    ('USER_CREATE', 'Create users'),
    ('USER_UPDATE', 'Update users'),
    ('USER_DELETE', 'Delete users'),

    ('ROLE_READ', 'Read roles'),
    ('ROLE_CREATE', 'Create roles'),
    ('ROLE_UPDATE', 'Update roles'),
    ('ROLE_DELETE', 'Delete roles'),

    ('PRODUCT_READ', 'Read products'),
    ('PRODUCT_CREATE', 'Create products'),
    ('PRODUCT_UPDATE', 'Update products'),
    ('PRODUCT_DELETE', 'Delete products');

-- =============================================================================
-- SEED DATA: ROLE_PERMISSIONS
-- =============================================================================

-- ADMIN -> tất cả permissions
INSERT INTO role_permissions(role_id, permission_id)
SELECT 1, id
FROM permissions;

-- MANAGER
INSERT INTO role_permissions(role_id, permission_id)
VALUES
    (2, 9),
    (2, 10),
    (2, 11),
    (2, 12);

-- USER
INSERT INTO role_permissions(role_id, permission_id)
VALUES
    (3, 1),
    (3, 9);

