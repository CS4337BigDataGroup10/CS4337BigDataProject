package com.example.AuthenticationService.exceptions;

public class TokenMissingException extends Exception {
    private final String errorMessage;
    private final String errorCode;

    public TokenMissingException() {
        this.errorMessage = "Token is invalid";
        this.errorCode = "TOKEN_MISSING";
    }

    public TokenMissingException(String message){
        this.errorMessage = message;
        this.errorCode = "TOKEN_MISSING";
    }

    public TokenMissingException(String message, String errorCode) {
        this.errorMessage = message;
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
