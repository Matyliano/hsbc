package com.example.hsbc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomResponse {

    private int status;

    private String message;

}