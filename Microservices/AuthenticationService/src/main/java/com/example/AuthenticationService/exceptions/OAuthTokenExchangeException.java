package com.example.AuthenticationService.exceptions;

public class OAuthTokenExchangeException extends Exception {
    private final String message;
    private final String errorCode;
    private final Throwable cause;

    public OAuthTokenExchangeException() {
        this.message = "OAuth Token Exchange Failed";
        this.errorCode = "OAUTH_TOKEN_EXCHANGE_FAILED";
        this.cause = null;
    }

    public OAuthTokenExchangeException(String message) {
        this.message = message;
        this.errorCode = "OAUTH_TOKEN_EXCHANGE_FAILED";
        this.cause = null;
    }

    public OAuthTokenExchangeException(String message, Throwable cause) {
        this.message = message;
        this.errorCode = "OAUTH_TOKEN_EXCHANGE_FAILED";
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Throwable getCause() {
        return cause;
    }
}
