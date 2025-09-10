--ALTER TABLE log_sheets ADD COLUMN user_id BIGINT;

-- And add the FK constraint if "users" table exists
--ALTER TABLE log_sheets
--    ADD CONSTRAINT fk_logsheet_user FOREIGN KEY (user_id) REFERENCES users(id);
