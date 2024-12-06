USE UserManagementDB;
CREATE TABLE IF NOT EXISTS user
(
    email         VARCHAR(255) PRIMARY KEY,
    given_name    VARCHAR(255) NOT NULL,
    family_name   VARCHAR(255) NOT NULL,
    is_tour_guide BOOLEAN      NOT NULL DEFAULT FALSE
);