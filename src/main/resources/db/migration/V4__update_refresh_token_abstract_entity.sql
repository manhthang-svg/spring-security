-- Thêm các cột kiểm toán (Auditing) và bổ trợ từ AbstractEntity
ALTER TABLE refresh_token
    ADD COLUMN created_by_id BIGINT NULL,
    ADD COLUMN updated_by_id BIGINT NULL,
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN version BIGINT DEFAULT 0;

-- Tạo các ràng buộc khóa ngoại (Foreign Key) trỏ tới bảng users
ALTER TABLE refresh_token
    ADD CONSTRAINT fk_refreshtoken_created_by
        FOREIGN KEY (created_by_id) REFERENCES users(id);

ALTER TABLE refresh_token
    ADD CONSTRAINT fk_refreshtoken_updated_by
        FOREIGN KEY (updated_by_id) REFERENCES users(id);