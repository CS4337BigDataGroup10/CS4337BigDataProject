CREATE TABLE User
(
    id  VARCHAR(255) PRIMARY KEY,
    email     VARCHAR(255) NOT NULL
);

CREATE TABLE TourBookings
(
    BookingId INT NOT NULL,
    TourId    INT NOT NULL,
    FOREIGN KEY (TourId) REFERENCES Tour (TourId)
);
CREATE TABLE Tour
(
    EmailID          VARCHAR(255) NOT NULL,
    TourId           INT AUTO_INCREMENT PRIMARY KEY,
    TourDateTime     DATETIME     NOT NULL,
    ParticipantCount INT CHECK (ParticipantCount <= 12),
    FOREIGN KEY (EmailID) REFERENCES User (EmailID)
);

CREATE TABLE Booking
(
    BookingId INT AUTO_INCREMENT PRIMARY KEY,
    EmailID   VARCHAR(255) NOT NULL,
    Size      INT          NOT NULL,
    FOREIGN KEY (EmailID) REFERENCES User (EmailID)
);
CREATE TABLE TourGuide
(
    EmailID  VARCHAR(255) PRIMARY KEY,
    AvailableDaysToWork SET('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'),
    Shifts   VARCHAR(255),
    Holidays INT DEFAULT 28,
    FOREIGN KEY (EmailID) REFERENCES User (EmailID)
);
