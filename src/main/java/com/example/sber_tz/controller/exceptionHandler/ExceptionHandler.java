package com.example.sber_tz.controller.exceptionHandler;

import com.example.sber_tz.exception.UserManipulationException;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(UserManipulationException.class)
    public ResponseEntity<?> handleUserManipulationException(UserManipulationException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse(ex.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException() {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse("ALL_FIELDS_ARE_REQUIRED"));
    }

    @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
    @JsonTypeName("error")
    public record ExceptionResponse(String message) {
    }
}
