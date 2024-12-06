package com.example.AuthenticationService.exceptions;

public class AuthenticationServiceExceptions {

    public static class OAuthTokenExchangeException extends RuntimeException {
        public OAuthTokenExchangeException(String message) {
            super(message);
        }

        public OAuthTokenExchangeException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class UserProfileRetrievalException extends RuntimeException {
        public UserProfileRetrievalException(String message) {
            super(message);
        }

        public UserProfileRetrievalException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }

        public UserNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class AuthenticationFailedException extends RuntimeException {
        public AuthenticationFailedException(String message) {
            super(message);
        }

        public AuthenticationFailedException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
