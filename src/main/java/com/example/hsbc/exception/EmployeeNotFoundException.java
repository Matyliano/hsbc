package com.example.hsbc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(String message, Long id) {
        super(message + "with id: " + id + " not found");
    }
    public EmployeeNotFoundException(String message) {
        super(message + " not found");
    }
}
