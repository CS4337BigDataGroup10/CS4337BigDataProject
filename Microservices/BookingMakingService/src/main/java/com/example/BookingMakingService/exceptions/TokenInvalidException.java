package com.example.BookingMakingService.exceptions;

public class TokenInvalidException extends RuntimeException {
    private final String errorCode;
    private final String message;

    public TokenInvalidException(String message) {
        this.errorCode = "TOKEN_INVALID";
        this.message = message;
    }

    public TokenInvalidException() {
        this.errorCode = "TOKEN_INVALID";
        this.message = "Token is missing";
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}
