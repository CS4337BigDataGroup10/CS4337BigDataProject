package com.example.AuthenticationService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class JwtServiceExceptions {

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpiredException(TokenExpiredException e) {
        return new ResponseEntity<>(
                new ErrorResponse("TOKEN_EXPIRED", e.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<ErrorResponse> handleTokenInvalidException(TokenInvalidException e) {
        return new ResponseEntity<>(
                new ErrorResponse("TOKEN_INVALID", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        return new ResponseEntity<>(
                new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred."),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
