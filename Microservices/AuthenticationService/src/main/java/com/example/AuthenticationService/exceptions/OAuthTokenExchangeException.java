package com.example.AuthenticationService.exceptions;

public class OAuthTokenExchangeException extends RuntimeException {
    private final String errorCode;
    private final String message;

    public OAuthTokenExchangeException() {
        this.errorCode = "OAUTH_TOKEN_EXCHANGE_FAILED";
        this.message = "OAuth Token Exchange Failed";
    }

    public OAuthTokenExchangeException(String message) {
        this.errorCode = "OAUTH_TOKEN_EXCHANGE_FAILED";
        this.message = message;
    }

    public OAuthTokenExchangeException(String message, Throwable cause) {
        this.errorCode = "OAUTH_TOKEN_EXCHANGE_FAILED";
        this.message = message + " | Cause: " + (cause != null ? cause.getMessage() : "Unknown");
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}
