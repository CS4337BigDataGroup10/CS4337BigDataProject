USE AuthServiceDB;

CREATE TABLE IF NOT EXISTS authenticated_users
(
    email                VARCHAR(255) PRIMARY KEY NOT NULL,
    refresh_token        TEXT,
    refresh_token_expiry TIMESTAMP
    );
