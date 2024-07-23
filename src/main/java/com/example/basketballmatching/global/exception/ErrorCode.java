package com.example.basketballmatching.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다."),


    ALREADY_EXIST_USER(HttpStatus.CONFLICT, "이미 존재하는 회원입니다.");


    private final HttpStatus httpStatus;
    private final String description;

}
