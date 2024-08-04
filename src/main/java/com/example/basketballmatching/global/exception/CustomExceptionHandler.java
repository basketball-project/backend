package com.example.basketballmatching.global.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleMyException(CustomException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());
        HttpStatus httpStatus = HttpStatus.valueOf(e.getErrorCode().getHttpStatus());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(
            MethodArgumentNotValidException exception) {

        ErrorResponse errorResponse =
                makeErrorResponse(exception.getBindingResult());

        return new ResponseEntity<>(errorResponse,
                HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse makeErrorResponse(BindingResult bindingResult) {

        ErrorCode errorCode = null;
        List<String> details = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                String field = error.getField();
                String defaultMessage = error.getDefaultMessage();
                String bindResultCode = error.getCode();

                errorCode = switch (Objects.requireNonNull(bindResultCode)) {
                    case "NotBlank" -> ErrorCode.INVALID_INPUT;
                    case "Email" -> ErrorCode.INVALID_PATTERN;
                    default -> ErrorCode.INTERNAL_SERVER_ERROR;
                };

                details.add(field + " : " + defaultMessage);
            }
        }

        return new ErrorResponse(errorCode, details);
    }
}
