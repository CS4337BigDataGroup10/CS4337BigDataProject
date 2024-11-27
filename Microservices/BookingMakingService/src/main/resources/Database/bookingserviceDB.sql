CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    email_id VARCHAR(255) NOT NULL,
    tour_id INT NOT NULL,
    is_cancelled BOOLEAN NOT NULL DEFAULT FALSE
);
