
-- User table no longer needed here. Request emailid from authentication service. Request name and usertype (via /{email}/is-tour-guide endpoint from UserManagement)

CREATE TABLE Tour
(
    TourId INT AUTO_INCREMENT PRIMARY KEY,
    TourGuideID VARCHAR(255), -- email id for tour guide assigned to tour
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

