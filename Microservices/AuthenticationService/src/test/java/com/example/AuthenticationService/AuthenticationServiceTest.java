package com.example.AuthenticationService;

import com.example.AuthenticationService.Objects.oAuthUser;
import com.example.AuthenticationService.Objects.oAuthResponse;
import com.example.AuthenticationService.dto.UserDTO;
import com.example.AuthenticationService.entity.UserEntity;
import com.example.AuthenticationService.repository.UserRepository;
import com.example.AuthenticationService.service.AuthenticationService;
import com.example.AuthenticationService.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private UserRepository userRepository;
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        jwtService = mock(JwtService.class);
        authenticationService = new AuthenticationService(
                "mock-client-id",
                "mock-client-secret",
                "http://mock-redirect-url",
                userRepository,
                jwtService
        );
    }

    @Test
    void testCheckIfUserExistsInDB_UserExists() {
        // Arrange
        String email = "test@example.com";
        UserEntity existingUser = new UserEntity();
        existingUser.setEmail(email);
        existingUser.setRefreshToken(UUID.randomUUID().toString());
        existingUser.setRefreshTokenExpiry(LocalDateTime.now().plusDays(1));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        UserEntity result = authenticationService.checkIfUserExistsInDB(email);

        assertEquals(existingUser, result);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void testCheckIfUserExistsInDB_UserDoesNotExist() {

        String email = "newuser@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);


        UserEntity result = authenticationService.checkIfUserExistsInDB(email);


        verify(userRepository).save(userCaptor.capture());
        UserEntity savedUser = userCaptor.getValue();
        assertEquals(email, savedUser.getEmail());
        assertNotNull(savedUser.getRefreshToken());
        assertNotNull(savedUser.getRefreshTokenExpiry());
    }

    @Test
    void testCheckIfRefreshTokenIsExpired_TokenNotExpired() {

        String email = "test@example.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setRefreshToken(UUID.randomUUID().toString());
        user.setRefreshTokenExpiry(LocalDateTime.now().plusDays(1));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));


        authenticationService.checkIfRefreshTokenIsExpired(email);


        verify(userRepository, never()).save(any(UserEntity.class)); // No changes made
    }

    @Test
    void testCheckIfRefreshTokenIsExpired_TokenExpired() {
        String email = "test@example.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setRefreshToken(UUID.randomUUID().toString());
        user.setRefreshTokenExpiry(LocalDateTime.now().minusDays(1));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        authenticationService.checkIfRefreshTokenIsExpired(email);


        verify(userRepository).save(user); // Ensure the user is saved after updating the token
        assertNotNull(user.getRefreshToken());
        assertTrue(user.getRefreshTokenExpiry().isAfter(LocalDateTime.now()));
    }

    @Test
    void testAuthenticationHandler() {
        String code = "mock-code";
        String mockAccessToken = "mock-access-token";
        String mockJwtToken = "mock-jwt-token";
        String email = "test@example.com";

        oAuthResponse mockResponse = new oAuthResponse();
        mockResponse.setAccess_token(mockAccessToken);

        oAuthUser mockUser = new oAuthUser();
        mockUser.setEmail(email);
        mockUser.setGiven_name("John");
        mockUser.setFamily_name("Doe");
        mockUser.setPicture("http://mock-picture-url");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setRefreshToken(UUID.randomUUID().toString());
        userEntity.setRefreshTokenExpiry(LocalDateTime.now().plusDays(7));

        when(jwtService.generateToken(email)).thenReturn(mockJwtToken);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        AuthenticationService spyService = Mockito.spy(authenticationService);
        doReturn(mockResponse).when(spyService).codeExchangeFromOauth(code);
        doReturn(mockUser).when(spyService).getProfileDetailsGoogle(mockAccessToken);

        Map<String, Object> result = spyService.authenticationHandler(code);

        assertEquals(mockJwtToken, result.get("jwtToken"));
        UserDTO userDto = (UserDTO) result.get("userDto");
        assertEquals(email, userDto.getEmail());
        assertEquals("John", userDto.getGivenName());
        assertEquals("Doe", userDto.getFamilyName());
        assertEquals("http://mock-picture-url", userDto.getProfilePicture());
    }
}
