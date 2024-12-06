package com.example.UserManagementService;

import com.example.UserManagementService.controller.UserController;
import com.example.UserManagementService.dto.CreateUserRequest;
import com.example.UserManagementService.dto.UpdateUserNameRequest;
import com.example.UserManagementService.dto.UserDTO;
import com.example.UserManagementService.entity.UserEntity;
import com.example.UserManagementService.exceptions.UserManagementServiceExceptions;
import com.example.UserManagementService.repository.UserManagementRepository;
import com.example.UserManagementService.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService; // Mocking the service layer

    @InjectMocks
    private UserController userController; // Injecting mocks into the controller

    // Endpoint to create a new user
    @Test
    void testCreateUser_Success() {
        CreateUserRequest request = new CreateUserRequest("John", "Doe", "john.doe@example.com");
        UserEntity userEntity = new UserEntity("john.doe@example.com", "John", "Doe", false);

        when(userService.createUser("John", "Doe", "john.doe@example.com")).thenReturn(userEntity);

        ResponseEntity<?> response = userController.createUser(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userEntity, response.getBody());
        verify(userService, times(1)).createUser("John", "Doe", "john.doe@example.com");
    }

    // Endpoint to update the name of an existing user
    @Test
    void testUpdateUserName_Success() {
        String email = "john.doe@example.com";
        when(userService.updateUserName(email, "John", "Doe")).thenReturn(true);

        ResponseEntity<?> response = userController.updateUserName(email, "John", "Doe");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User name updated successfully", response.getBody());
        verify(userService, times(1)).updateUserName(email, "John", "Doe");
    }

    @Test
    void testUpdateUserName_UserNotFound() {
        String email = "john.doe@example.com";
        when(userService.updateUserName(email, "John", "Doe")).thenReturn(false);

        ResponseEntity<?> response = userController.updateUserName(email, "John", "Doe");

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).updateUserName(email, "John", "Doe");
    }


    // Endpoint to delete a user
    @Test
    void testDeleteUser_Success() {
        String email = "john.doe@example.com";
        when(userService.deleteUser(email)).thenReturn(true);

        ResponseEntity<String> response = userController.deleteUser(email);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User successfully deleted.", response.getBody());
        verify(userService, times(1)).deleteUser(email);
    }

    @Test
    void testDeleteUser_NotFound() {
        String email = "john.doe@example.com";
        when(userService.deleteUser(email)).thenReturn(false);

        ResponseEntity<String> response = userController.deleteUser(email);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found.", response.getBody());
        verify(userService, times(1)).deleteUser(email);
    }

    // Endpoint to handle user login and create a user
    @Test
    void testHandleNewUserAuth_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("john.doe@example.com");
        userDTO.setGivenName("John");
        userDTO.setFamilyName("Doe");
        userDTO.setProfilePicture("http://example.com/profile.jpg");
        userDTO.setTourGuide(false);

        UserEntity userEntity = new UserEntity(
                "john.doe@example.com",
                "John",
                "Doe",
                false
        );

        when(userService.handleNewUserLogin(userDTO)).thenReturn(userEntity);

        ResponseEntity<UserEntity> response = userController.handleNewUserAuth(userDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(userDTO.getEmail(), response.getBody().getEmail());
        assertEquals(userDTO.getGivenName(), response.getBody().getGivenName());  // Assuming name format
        assertEquals(userDTO.getFamilyName(), response.getBody().getFamilyName()); // Assuming name format
        assertEquals(userDTO.isTourGuide(), response.getBody().isTourGuide());

        verify(userService, times(1)).handleNewUserLogin(userDTO);
    }


    // Endpoint to become a tour guide
    @Test
    void testBecomeTourGuide_Success() {
        String email = "john.doe@example.com";
        when(userService.becomeTourGuide(email, "password123")).thenReturn(true);

        ResponseEntity<String> response = userController.becomeTourGuide(email, "password123");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User is now a tour guide", response.getBody());
        verify(userService, times(1)).becomeTourGuide(email, "password123");
    }

    @Test
    void testBecomeTourGuide_Failure() {
        String email = "john.doe@example.com";
        when(userService.becomeTourGuide(email, "wrongpassword")).thenReturn(false);

        ResponseEntity<String> response = userController.becomeTourGuide(email, "wrongpassword");

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Failed to become a tour guide: incorrect password or user not found", response.getBody());
        verify(userService, times(1)).becomeTourGuide(email, "wrongpassword");
    }

    // Endpoint to check if a user is a tour guide
    @Test
    void testIsTourGuide_True() {
        String email = "john.doe@example.com";
        when(userService.isTourGuide(email)).thenReturn(true);

        ResponseEntity<Boolean> response = userController.isTourGuide(email);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
        verify(userService, times(1)).isTourGuide(email);
    }

    @Test
    void testIsTourGuide_False() {
        String email = "john.doe@example.com";
        when(userService.isTourGuide(email)).thenReturn(false);

        ResponseEntity<Boolean> response = userController.isTourGuide(email);

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody());
        verify(userService, times(1)).isTourGuide(email);
    }

}

@ExtendWith(MockitoExtension.class)
class UserManagementServiceExceptionsTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    // Test exception handling in createUser
    @Test
    void testCreateUser_ServiceThrowsException() {
        CreateUserRequest request = new CreateUserRequest("John", "Doe", "john.doe@example.com");

        when(userService.createUser(anyString(), anyString(), anyString()))
                .thenThrow(new UserManagementServiceExceptions("Service exception occurred"));

        Exception exception = assertThrows(UserManagementServiceExceptions.class, () -> {
            userController.createUser(request);
        });

        assertEquals("Service exception occurred", exception.getMessage());
        verify(userService, times(1)).createUser("John", "Doe", "john.doe@example.com");
    }

    // Test exception handling in deleteUser
    @Test
    void testDeleteUser_ServiceThrowsException() {
        String email = "john.doe@example.com";

        when(userService.deleteUser(email)).thenThrow(new UserManagementServiceExceptions("User deletion failed"));

        Exception exception = assertThrows(UserManagementServiceExceptions.class, () -> {
            userController.deleteUser(email);
        });

        assertEquals("User deletion failed", exception.getMessage());
        verify(userService, times(1)).deleteUser(email);
    }

    // Test exception handling in becomeTourGuide
    @Test
    void testBecomeTourGuide_ServiceThrowsException() {
        String email = "john.doe@example.com";

        when(userService.becomeTourGuide(email, "password123"))
                .thenThrow(new UserManagementServiceExceptions("Invalid credentials"));

        Exception exception = assertThrows(UserManagementServiceExceptions.class, () -> {
            userController.becomeTourGuide(email, "password123");
        });

        assertEquals("Invalid credentials", exception.getMessage());
        verify(userService, times(1)).becomeTourGuide(email, "password123");
    }

    // Test exception handling in updateUserName
    @Test
    void testUpdateUserName_ServiceThrowsException() {
        String email = "john.doe@example.com";

        when(userService.updateUserName(email, "John", "Doe"))
                .thenThrow(new UserManagementServiceExceptions("Update failed"));

        Exception exception = assertThrows(UserManagementServiceExceptions.class, () -> {
            userController.updateUserName(email, "John", "Doe");
        });

        assertEquals("Update failed", exception.getMessage());
        verify(userService, times(1)).updateUserName(email, "John", "Doe");
    }

    // Test exception handling in handleNewUserAuth
    @Test
    void testHandleNewUserAuth_ServiceThrowsException() {
        when(userService.handleNewUserLogin(any()))
                .thenThrow(new UserManagementServiceExceptions("Login failed"));

        Exception exception = assertThrows(UserManagementServiceExceptions.class, () -> {
            userController.handleNewUserAuth(null);  // Test case where request is null for example
        });

        assertEquals("Login failed", exception.getMessage());
        verify(userService, times(1)).handleNewUserLogin(any());
    }
}

class UserEntityTest {

    @Test
    void testUserEntityConstructor() {
        // Set up the constructor with test values
        String email = "john.doe@example.com";
        String givenName = "John";
        String familyName = "Doe";
        boolean isTourGuide = false;
        UserEntity userEntity = new UserEntity(email, givenName, familyName, isTourGuide);
        assertEquals(email, userEntity.getEmail());
        assertEquals(givenName, userEntity.getGivenName());
        assertEquals(familyName, userEntity.getFamilyName());
        assertFalse(userEntity.isTourGuide());
    }

    @Test //simple tests for the getters and setters
    void testSettersAndGetters() {
        UserEntity userEntity = new UserEntity();
        String newEmail = "jane.doe@example.com";
        String newGivenName = "Jane";
        String newFamilyName = "Doe";
        boolean newIsTourGuide = true;
        userEntity.setEmail(newEmail);
        userEntity.setGivenName(newGivenName);
        userEntity.setFamilyName(newFamilyName);
        userEntity.setIsTourGuide(newIsTourGuide);
        assertEquals(newEmail, userEntity.getEmail());
        assertEquals(newGivenName, userEntity.getGivenName());
        assertEquals(newFamilyName, userEntity.getFamilyName());
        assertTrue(userEntity.isTourGuide());
    }

    @Test // test the default constructor
    void testUserEntityDefaultConstructor() {
        UserEntity userEntity = new UserEntity();
        assertNull(userEntity.getEmail());
        assertNull(userEntity.getGivenName());
        assertNull(userEntity.getFamilyName());
        assertFalse(userEntity.isTourGuide());
    }
}




@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserManagementRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test //Testing the create user function
    void testCreateUser() {
        String email = "test@example.com";
        String givenName = "Test";
        String familyName = "User";
        UserEntity user = new UserEntity(email, givenName, familyName, false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        UserEntity createdUser = userService.createUser(givenName, familyName, email);
        assertNotNull(createdUser);
        assertEquals(email, createdUser.getEmail());
        assertEquals(givenName, createdUser.getGivenName());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test //Test for existing user trying to log in
    void testHandleNewUserLogin_existingUser() {
        UserDTO userDTO = new UserDTO("test@example.com", "Test", "User", "default.jpg", false);
        UserEntity existingUser = new UserEntity("test@example.com", "Test", "User", false);
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(existingUser));
        UserEntity returnedUser = userService.handleNewUserLogin(userDTO);
        assertEquals(existingUser.getEmail(), returnedUser.getEmail());
        verify(userRepository, times(1)).findById("test@example.com");
    }

    @Test //Test to update username
    void testUpdateUserName_userExists() {
        String email = "test@example.com";
        String newGivenName = "NewName";
        String newFamilyName = "NewFamily";
        UserEntity user = new UserEntity(email, "OldName", "OldFamily", false);
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        boolean updated = userService.updateUserName(email, newGivenName, newFamilyName);
        assertTrue(updated);
        assertEquals(newGivenName, user.getGivenName());
        assertEquals(newFamilyName, user.getFamilyName());
    }

    // Tests for trying to delete user
    @Test  //
    void testDeleteUser_userExists() {
        String email = "test@example.com";
        UserEntity user = new UserEntity(email, "Test", "User", false);
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user));
        boolean deleted = userService.deleteUser(email);
        assertTrue(deleted);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_userNotFound() {
        String email = "test@example.com";
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());
        boolean deleted = userService.deleteUser(email);
        assertFalse(deleted);
    }

    @Test //Test for the becomeTourGuide function when the correct password is provided
    void testBecomeTourGuide_success() {
        String email = "test@example.com";
        String password = "1234";
        UserEntity user = new UserEntity(email, "Test", "User", false);
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        boolean result = userService.becomeTourGuide(email, password);
        assertTrue(result);
        assertTrue(user.isTourGuide());
    }

    @Test //Test for the becomeTourGuide function when the incorrect password is provided
    void testBecomeTourGuide_failIncorrectPassword() {
        String email = "test@example.com";
        String password = "wrongPassword";
        UserEntity user = new UserEntity(email, "Test", "User", false);
        // lenient is necessary to prevent snubbing error
        lenient().when(userRepository.findById(email)).thenReturn(Optional.of(user));
        boolean result = userService.becomeTourGuide(email, password);
        assertFalse(result);
        assertFalse(user.isTourGuide());
    }

    @Test // test for the isTourGuide function
    void testIsTourGuide() {
        String email = "test@example.com";
        UserEntity user = new UserEntity(email, "Test", "User", true);
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        boolean isTourGuide = userService.isTourGuide(email);
        assertTrue(isTourGuide);
    }

    @Test //null test
    void testIsTourGuide_userNotFound() {
        String email = "test@example.com";
        when(userRepository.findById(email)).thenReturn(Optional.empty());
        assertThrows(UserManagementServiceExceptions.class, () -> userService.isTourGuide(email));
    }
}

class UpdateUserNameRequestTest {
    @Test
    void testSetAndGetName() {
        // Create an instance of the UpdateUserNameRequest class
        UpdateUserNameRequest request = new UpdateUserNameRequest();

        // Set the name using the setter
        String testName = "John Doe";
        request.setName(testName);

        // Assert that the getter returns the expected value
        assertEquals(testName, request.getName(), "The name should be set and retrieved correctly");
    }

    // Test that the default state of the name field is null
    @Test
    void testDefaultNameIsNull() {
        UpdateUserNameRequest request = new UpdateUserNameRequest();
        assertNull(request.getName(), "The default name should be null");
    }

}

