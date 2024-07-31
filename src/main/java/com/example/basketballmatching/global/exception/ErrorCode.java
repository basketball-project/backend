package com.example.basketballmatching.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다"),
    INVALID_AUTH_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 코드입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    ALREADY_EXIST_USER(HttpStatus.CONFLICT, "이미 존재하는 회원입니다.");


    private final HttpStatus httpStatus;
    private final String description;

}
