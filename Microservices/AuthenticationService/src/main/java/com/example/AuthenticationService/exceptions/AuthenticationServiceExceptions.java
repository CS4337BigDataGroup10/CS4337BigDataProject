package com.example.AuthenticationService.exceptions;

public class AuthenticationServiceExceptions {

    // Exception for when the OAuth access token exchange fails
    public static class OAuthTokenExchangeException extends RuntimeException {
        public OAuthTokenExchangeException(String message) {
            super(message);
        }

        public OAuthTokenExchangeException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // Exception for when the user profile details retrieval fails
    public static class UserProfileRetrievalException extends RuntimeException {
        public UserProfileRetrievalException(String message) {
            super(message);
        }

        public UserProfileRetrievalException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // Exception for when the user is not found in the database
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }

        public UserNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // Exception for generic authentication failures
    public static class AuthenticationFailedException extends RuntimeException {
        public AuthenticationFailedException(String message) {
            super(message);
        }

        public AuthenticationFailedException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
