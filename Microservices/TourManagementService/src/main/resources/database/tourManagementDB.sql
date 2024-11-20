CREATE TABLE User
(
    EmailID  VARCHAR(255) PRIMARY KEY,
    Name     VARCHAR(255) NOT NULL,
    UserType VARCHAR(20) NOT NULL CHECK (UserType IN ('user', 'tour guide'))
);

CREATE TABLE Tour
(
    TourId INT AUTO_INCREMENT PRIMARY KEY,
    EmailID VARCHAR(255),
    TourDateTime DATETIME NOT NULL,
    ParticipantCount INT DEFAULT 0,
    MaxCapacity INT DEFAULT 20 CHECK (MaxCapacity <= 20),
    FOREIGN KEY (EmailID) REFERENCES User (EmailID) ON DELETE SET NULL
);

CREATE TABLE TourGuide (
    EmailID VARCHAR(255) PRIMARY KEY,
    AvailableDaysToWork SET('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'),
    FOREIGN KEY (EmailID) REFERENCES User (EmailID) ON DELETE CASCADE
);

