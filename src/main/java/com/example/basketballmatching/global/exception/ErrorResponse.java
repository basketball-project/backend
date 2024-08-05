package com.example.basketballmatching.global.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private int statusCode;
    private ErrorCode errorCode;
    private String errorMessage;
    private List<String> details;

    public ErrorResponse(ErrorCode errorCode) {
        this.statusCode = errorCode.getHttpStatus();
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

    public ErrorResponse(ErrorCode errorCode, List<String> details) {
        this.statusCode = errorCode.getHttpStatus();
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
        this.details = details;
    }

}
