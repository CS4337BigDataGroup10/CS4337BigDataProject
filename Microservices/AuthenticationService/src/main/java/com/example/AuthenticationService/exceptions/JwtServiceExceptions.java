package com.example.AuthenticationService.exceptions;

public class JwtServiceExceptions {

    public static class TokenExpiredException extends RuntimeException {
        public TokenExpiredException(String message) {
            super(message);
        }

        public TokenExpiredException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String message) {
            super(message);
        }

        public InvalidTokenException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
