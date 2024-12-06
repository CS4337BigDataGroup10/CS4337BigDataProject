package com.example.BookingMakingService.exceptions;

public class TokenExpiredException extends RuntimeException {
    private final String errorCode;
    private final String message;

    public TokenExpiredException(String message) {
        this.errorCode = "TOKEN_EXPIRED";
        this.message = message;
    }

    public TokenExpiredException() {
        this.errorCode = "TOKEN_EXPIRED";
        this.message = "Token is expired";
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}
