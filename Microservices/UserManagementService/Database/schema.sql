USE UserManagementDB;
CREATE TABLE User
(
    email         VARCHAR(255) PRIMARY KEY,
    given_name    VARCHAR(255) NOT NULL,
    family_name   VARCHAR(255) NOT NULL,
    is_tour_guide BOOLEAN      NOT NULL DEFAULT FALSE
);