-- =============================================================================
-- V1__init_rbac.sql
-- Khởi tạo schema RBAC hoàn chỉnh
-- Bao gồm:
--   - users
--   - roles
--   - permissions
--   - user_roles
--   - role_permissions
--   - refresh_token
--
-- AbstractEntity:
--   id
--   created_by_id
--   updated_by_id
--   created_at
--   updated_at
--   deleted
--   version
-- =============================================================================

-- =============================================================================
-- USERS
-- =============================================================================
CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,

                       username VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       enabled BOOLEAN NOT NULL DEFAULT TRUE,
                       account_locked BOOLEAN NOT NULL DEFAULT FALSE,
                       account_expired BOOLEAN NOT NULL DEFAULT FALSE,

    -- Audit fields
                       created_by_id BIGINT NULL,
                       updated_by_id BIGINT NULL,

                       created_at DATETIME(6)
                           NOT NULL
                                                DEFAULT CURRENT_TIMESTAMP(6),

                       updated_at DATETIME(6)
                           NOT NULL
                                                DEFAULT CURRENT_TIMESTAMP(6)
                           ON UPDATE CURRENT_TIMESTAMP(6),

                       deleted BOOLEAN NOT NULL DEFAULT FALSE,

                       version BIGINT NOT NULL DEFAULT 0,

                       PRIMARY KEY (id),

                       CONSTRAINT fk_users_created_by
                           FOREIGN KEY (created_by_id)
                               REFERENCES users(id),

                       CONSTRAINT fk_users_updated_by
                           FOREIGN KEY (updated_by_id)
                               REFERENCES users(id)
);

-- =============================================================================
-- ROLES
-- =============================================================================
CREATE TABLE roles (
                       id BIGINT NOT NULL AUTO_INCREMENT,

                       name VARCHAR(50) NOT NULL UNIQUE,

    -- Audit fields
                       created_by_id BIGINT NULL,
                       updated_by_id BIGINT NULL,

                       created_at DATETIME(6)
                           NOT NULL
                                                DEFAULT CURRENT_TIMESTAMP(6),

                       updated_at DATETIME(6)
                           NOT NULL
                                                DEFAULT CURRENT_TIMESTAMP(6)
                           ON UPDATE CURRENT_TIMESTAMP(6),

                       deleted BOOLEAN NOT NULL DEFAULT FALSE,

                       version BIGINT NOT NULL DEFAULT 0,

                       PRIMARY KEY (id),

                       CONSTRAINT fk_roles_created_by
                           FOREIGN KEY (created_by_id)
                               REFERENCES users(id),

                       CONSTRAINT fk_roles_updated_by
                           FOREIGN KEY (updated_by_id)
                               REFERENCES users(id)
);

-- =============================================================================
-- PERMISSIONS
-- =============================================================================
CREATE TABLE permissions (
                             id BIGINT NOT NULL AUTO_INCREMENT,

                             name VARCHAR(100) NOT NULL UNIQUE,
                             description VARCHAR(255) NULL,

    -- Audit fields
                             created_by_id BIGINT NULL,
                             updated_by_id BIGINT NULL,

                             created_at DATETIME(6)
                                 NOT NULL
                                                      DEFAULT CURRENT_TIMESTAMP(6),

                             updated_at DATETIME(6)
                                 NOT NULL
                                                      DEFAULT CURRENT_TIMESTAMP(6)
                                 ON UPDATE CURRENT_TIMESTAMP(6),

                             deleted BOOLEAN NOT NULL DEFAULT FALSE,

                             version BIGINT NOT NULL DEFAULT 0,

                             PRIMARY KEY (id),

                             CONSTRAINT fk_permissions_created_by
                                 FOREIGN KEY (created_by_id)
                                     REFERENCES users(id),

                             CONSTRAINT fk_permissions_updated_by
                                 FOREIGN KEY (updated_by_id)
                                     REFERENCES users(id)
);

-- =============================================================================
-- USER_ROLES
-- Many-to-Many:
-- users <-> roles
-- =============================================================================
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,

                            PRIMARY KEY (user_id, role_id),

                            CONSTRAINT fk_user_roles_user
                                FOREIGN KEY (user_id)
                                    REFERENCES users(id)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_user_roles_role
                                FOREIGN KEY (role_id)
                                    REFERENCES roles(id)
                                    ON DELETE CASCADE
);

-- =============================================================================
-- ROLE_PERMISSIONS
-- Many-to-Many:
-- roles <-> permissions
-- =============================================================================
CREATE TABLE role_permissions (
                                  role_id BIGINT NOT NULL,
                                  permission_id BIGINT NOT NULL,

                                  PRIMARY KEY (role_id, permission_id),

                                  CONSTRAINT fk_role_permissions_role
                                      FOREIGN KEY (role_id)
                                          REFERENCES roles(id)
                                          ON DELETE CASCADE,

                                  CONSTRAINT fk_role_permissions_permission
                                      FOREIGN KEY (permission_id)
                                          REFERENCES permissions(id)
                                          ON DELETE CASCADE
);

-- =============================================================================
-- INDEXES
-- =============================================================================

CREATE INDEX idx_users_created_by
    ON users(created_by_id);

CREATE INDEX idx_users_updated_by
    ON users(updated_by_id);

CREATE INDEX idx_roles_created_by
    ON roles(created_by_id);

CREATE INDEX idx_roles_updated_by
    ON roles(updated_by_id);

CREATE INDEX idx_permissions_created_by
    ON permissions(created_by_id);

CREATE INDEX idx_permissions_updated_by
    ON permissions(updated_by_id);

-- =============================================================================
-- REFRESH_TOKEN
-- =============================================================================
CREATE TABLE refresh_token (
                               id BIGINT NOT NULL AUTO_INCREMENT,

                               token VARCHAR(255) NOT NULL UNIQUE,
                               user_id BIGINT NOT NULL UNIQUE,
                               expiry_date TIMESTAMP NOT NULL,

    -- Audit fields
                               created_by_id BIGINT NULL,
                               updated_by_id BIGINT NULL,

                               created_at TIMESTAMP
                                   NOT NULL
                                                        DEFAULT CURRENT_TIMESTAMP,

                               updated_at TIMESTAMP
                                   NOT NULL
                                                        DEFAULT CURRENT_TIMESTAMP
                                   ON UPDATE CURRENT_TIMESTAMP,

                               deleted BOOLEAN NOT NULL DEFAULT FALSE,

                               version BIGINT DEFAULT 0,

                               PRIMARY KEY (id),

                               CONSTRAINT fk_refresh_token_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES users(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_refreshtoken_created_by
                                   FOREIGN KEY (created_by_id)
                                       REFERENCES users(id),

                               CONSTRAINT fk_refreshtoken_updated_by
                                   FOREIGN KEY (updated_by_id)
                                       REFERENCES users(id)
);

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

-- ADMIN -> all permissions
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
