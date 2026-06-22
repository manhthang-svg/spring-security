-- V2__add_account_status_fields.sql

ALTER TABLE users
    ADD COLUMN enabled BOOLEAN,
ADD COLUMN account_locked BOOLEAN,
ADD COLUMN account_expired BOOLEAN;

UPDATE users
SET
    enabled = TRUE,
    account_locked = FALSE,
    account_expired = FALSE
WHERE enabled IS NULL;

ALTER TABLE users
    MODIFY enabled BOOLEAN NOT NULL DEFAULT TRUE,
    MODIFY account_locked BOOLEAN NOT NULL DEFAULT FALSE,
    MODIFY account_expired BOOLEAN NOT NULL DEFAULT FALSE;