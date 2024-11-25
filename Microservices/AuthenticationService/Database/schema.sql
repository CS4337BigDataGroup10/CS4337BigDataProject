USE AuthServiceDB;


CREATE TABLE AuthenticatedUsers
(
    email                VARCHAR(255) PRIMARY KEY NOT NULL,
    refresh_token        TEXT,
    refresh_token_expiry TIMESTAMP
);

