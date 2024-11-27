CREATE DATABASE IF NOT EXISTS TourManagementDB;

USE TourManagementDB;

CREATE TABLE User (
                        email_id VARCHAR(255) NOT NULL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        user_type VARCHAR(20) NOT NULL CHECK (user_type IN ('user', 'tour guide'))
);

CREATE TABLE Tour (
                      tour_id INT AUTO_INCREMENT PRIMARY KEY,
                      email_id VARCHAR(255),
                      tour_date_time DATETIME NOT NULL,
                      participant_count INT NOT NULL,
                      max_capacity INT NOT NULL,
                      FOREIGN KEY (email_id) REFERENCES User(email_id)
);

CREATE TABLE Booking (
                         booking_id INT AUTO_INCREMENT PRIMARY KEY,
                         email_id VARCHAR(255) NOT NULL,
                         tour_id INT NOT NULL,
                         size INT NOT NULL,
                         FOREIGN KEY (email_id) REFERENCES User(email_id),
                         FOREIGN KEY (tour_id) REFERENCES Tour(tour_id)
);

CREATE TABLE TourBookings (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              booking_id INT NOT NULL,
                              tour_id INT NOT NULL,
                              FOREIGN KEY (booking_id) REFERENCES Booking(booking_id),
                              FOREIGN KEY (tour_id) REFERENCES Tour(tour_id)
);
