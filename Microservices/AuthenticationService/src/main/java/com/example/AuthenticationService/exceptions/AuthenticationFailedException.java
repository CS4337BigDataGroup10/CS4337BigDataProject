package com.example.AuthenticationService.exceptions;

public class AuthenticationFailedException extends Exception{
    private final String message;
    private final String errorCode;

    public AuthenticationFailedException(String message) {
        this.message = message;
        this.errorCode = "AUTHENTICATION_FAILED";
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
