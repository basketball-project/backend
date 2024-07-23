package com.example.basketballmatching.global.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.basketballmatching.global.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(CustomException.class)
    public ErrorResponse handleCustomException(CustomException e) {
        log.error("CustomException is occurred." + e);

        return new ErrorResponse(e.getErrorCode(),
                e.getErrorCode().getHttpStatus(), e.getErrorCode().getDescription());
    }


    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        log.error("Exception is occurred." + e);
        return new ErrorResponse(INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getDescription());
    }

}
