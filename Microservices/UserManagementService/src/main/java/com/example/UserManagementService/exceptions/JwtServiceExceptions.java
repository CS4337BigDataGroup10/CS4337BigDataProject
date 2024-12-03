package com.example.UserManagementService.exceptions;

public class
JwtServiceExceptions {

    // Exception for expired JWT tokens
    public static class TokenExpiredException extends RuntimeException {
        public TokenExpiredException(String message) {
            super(message);
        }

        public TokenExpiredException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // Exception for invalid JWT tokens
    public static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String message) {
            super(message);
        }

        public InvalidTokenException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
