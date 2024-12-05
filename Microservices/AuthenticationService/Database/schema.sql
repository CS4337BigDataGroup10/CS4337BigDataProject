USE AuthServiceDB;

CREATE TABLE IF NOT EXISTS AuthenticatedUsers
(
    email                VARCHAR(255) PRIMARY KEY NOT NULL,
    refresh_token        TEXT,
    refresh_token_expiry TIMESTAMP
    );
