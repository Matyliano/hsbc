package com.example.hsbc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;




@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {



    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomResponse> handleNotFoundException(Exception ex) {
        CustomResponse error = new CustomResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse> handleBadRequestException() {
        CustomResponse error = new CustomResponse(HttpStatus.BAD_REQUEST.value(),
                "Your request has issued a malformed or illegal request.");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<String> internalServerErrorHandler(HttpServerErrorException ex) {
        String bodyOfResponse = ex.getResponseBodyAsString();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(bodyOfResponse);
    }
}
