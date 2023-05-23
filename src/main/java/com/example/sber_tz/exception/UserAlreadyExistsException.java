package com.example.sber_tz.exception;

public class UserAlreadyExistsException extends UserManipulationException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
