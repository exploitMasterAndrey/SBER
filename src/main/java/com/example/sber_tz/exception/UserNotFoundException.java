package com.example.sber_tz.exception;

public class UserNotFoundException extends UserManipulationException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
