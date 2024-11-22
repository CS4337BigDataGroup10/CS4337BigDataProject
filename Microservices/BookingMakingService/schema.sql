USE BookingServiceDB;


CREATE TABLE IF NOT EXISTS Booking (
    bookingId INT AUTO_INCREMENT PRIMARY KEY,
    emailId VARCHAR(255) NOT NULL,
    tourId INT NOT NULL
    );