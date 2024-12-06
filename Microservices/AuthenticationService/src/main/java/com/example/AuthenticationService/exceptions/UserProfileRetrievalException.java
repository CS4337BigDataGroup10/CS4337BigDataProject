package com.example.AuthenticationService.exceptions;

public class UserProfileRetrievalException extends Exception {
    private final String message;
    private final String errorCode;
    private final Throwable cause;

    public UserProfileRetrievalException(String message) {
        this.message = message;
        this.errorCode = "USER_PROFILE_RETRIEVAL_FAILED";
        this.cause = null;
    }

    public UserProfileRetrievalException(String message, Throwable cause) {
        this.message = message;
        this.errorCode = "USER_PROFILE_RETRIEVAL_FAILED";
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
