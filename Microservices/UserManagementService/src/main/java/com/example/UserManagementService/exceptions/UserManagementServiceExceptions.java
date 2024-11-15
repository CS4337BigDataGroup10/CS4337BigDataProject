package com.example.UserManagementService.exceptions;

public class UserManagementServiceExceptions extends RuntimeException {
    public UserManagementServiceExceptions(String message) {
        super(message);
    }

    public UserManagementServiceExceptions(String message, Throwable cause) {
        super(message, cause);
    }


}