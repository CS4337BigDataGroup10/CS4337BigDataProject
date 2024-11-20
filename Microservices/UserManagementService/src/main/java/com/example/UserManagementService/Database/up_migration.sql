CREATE TABLE users (
                       email VARCHAR(255) PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       is_tour_guide BOOLEAN NOT NULL DEFAULT FALSE
);

-- Optional index on the `is_tour_guide` field if needed
CREATE INDEX idx_is_tour_guide ON users(is_tour_guide);
