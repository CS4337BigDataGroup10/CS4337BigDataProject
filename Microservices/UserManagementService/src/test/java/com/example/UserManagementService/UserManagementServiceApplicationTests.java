package com.example.UserManagementService;
import com.example.UserManagementService.controller.UserController;
import com.example.UserManagementService.dto.CreateUserRequest;
import com.example.UserManagementService.dto.UserDTO;
import com.example.UserManagementService.entity.UserEntity;
import com.example.UserManagementService.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

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

    // Endpoint to ping
    @Test
    void testPing() {
        String response = userController.ping();

        assertEquals("Hello", response);
    }
}

