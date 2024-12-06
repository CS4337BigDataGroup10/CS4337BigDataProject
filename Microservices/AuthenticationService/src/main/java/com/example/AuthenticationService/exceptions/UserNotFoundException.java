package com.example.AuthenticationService.exceptions;

public class UserNotFoundException extends RuntimeException {
    private final String errorCode;
    private final String message;

    public UserNotFoundException(String message) {
        this.errorCode = "USER_NOT_FOUND";
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}
