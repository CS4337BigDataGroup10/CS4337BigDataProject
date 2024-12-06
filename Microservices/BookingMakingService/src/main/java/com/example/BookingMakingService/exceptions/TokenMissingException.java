package com.example.BookingMakingService.exceptions;

public class TokenMissingException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    public TokenMissingException() {
        this.errorCode = "TOKEN_MISSING";
        this.errorMessage = "Authorization token is missing or invalid.";
    }

    public TokenMissingException(String message) {
        this.errorCode = "TOKEN_MISSING";
        this.errorMessage = message;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getMessage() {
        return this.errorMessage;
    }
}
