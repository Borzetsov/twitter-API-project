ALTER TABLE users RENAME COLUMN username TO email;

ALTER TABLE users
    ADD CONSTRAINT uq_users_email UNIQUE (email);
