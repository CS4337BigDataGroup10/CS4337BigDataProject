package com.example.UserManagementService.exceptions;

public class TokenExpiredException extends Exception {
    private final String errorMessage;
    private final String errorCode;

    public TokenExpiredException() {
        this.errorMessage = "Token has expired";
        this.errorCode = "TOKEN_EXPIRED";
    }

    public TokenExpiredException(String message, String errorCode) {
        this.errorMessage = message;
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
