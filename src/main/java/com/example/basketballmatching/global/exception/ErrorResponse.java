package com.example.basketballmatching.global.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorResponse {


    private ErrorCode errorCode;
    private HttpStatus httpStatus;
    private String description;

}
