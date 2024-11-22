/*CREATE TABLE User
**(
**    EmailID  VARCHAR(255) PRIMARY KEY,
**    Name     VARCHAR(255) NOT NULL,
**    UserType VARCHAR(20) NOT NULL CHECK (UserType IN ('user', 'tour guide'))
);*/

-- User table no longer needed here. Request emailid from authentication service. Request name and usertype (via /{email}/is-tour-guide endpoint from UserManagement)

CREATE TABLE Tour
(
    TourId INT AUTO_INCREMENT PRIMARY KEY,
    EmailID VARCHAR(255), -- Is this for Tour Guide? Could be less ambiguous with a second name
    TourDateTime DATETIME NOT NULL,
    ParticipantCount INT DEFAULT 0,
    MaxCapacity INT DEFAULT 20 CHECK (MaxCapacity <= 20),
    FOREIGN KEY (EmailID) REFERENCES User (EmailID) ON DELETE SET NULL
);

-- TourBookings doesn't need a key and only exists for database normalisation. This is the table that keeps track of all the bookings on a tour
CREATE TABLE TourBookings (
TourId INT,
BookingID INT);


-- TourGuide table deprecated as no need for available days to work

/* CREATE TABLE TourGuide (
    EmailID VARCHAR(255) PRIMARY KEY,
    AvailableDaysToWork SET('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'),
    FOREIGN KEY (EmailID) REFERENCES User (EmailID) ON DELETE CASCADE
); */

