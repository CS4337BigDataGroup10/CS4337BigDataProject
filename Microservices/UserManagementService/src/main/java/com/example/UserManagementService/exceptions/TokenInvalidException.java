package com.example.UserManagementService.exceptions;

public class TokenInvalidException extends Exception {
    private final String errorMessage;
    private final String errorCode;

    public TokenInvalidException() {
        this.errorMessage = "Token is invalid";
        this.errorCode = "TOKEN_INVALID";
    }

    public TokenInvalidException(String message, String errorCode) {
        this.errorMessage = message;
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
