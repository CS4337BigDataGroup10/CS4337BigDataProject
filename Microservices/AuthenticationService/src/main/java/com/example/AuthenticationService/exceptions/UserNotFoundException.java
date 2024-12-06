package com.example.AuthenticationService.exceptions;

public class UserNotFoundException extends Exception {
    private final String message;
    private final String errorCode;

    public UserNotFoundException(String message) {
        this.message = message;
        this.errorCode = "USER_NOT_FOUND";
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

