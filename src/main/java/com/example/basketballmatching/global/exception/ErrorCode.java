package com.example.basketballmatching.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 이메일입니다"),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다."),
    ALREADY_EXIST_USER(HttpStatus.CONFLICT.value(), "이미 존재하는 회원입니다."),
    CONFIRM_EMAIL_AUTH(HttpStatus.BAD_REQUEST.value(), "이메일 인증을 확인해주세요."),
    INVALID_AUTH_CODE(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 이메일 인증 코드입니다."),
    ALREADY_LOGOUT(HttpStatus.BAD_REQUEST.value(), "이미 로그아웃 하였습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST.value(), "필수 입력 값이 누락되었습니다."),
    INVALID_PATTERN(HttpStatus.BAD_REQUEST.value(), "형식에 맞게 입력 해야합니다."),
    MAIL_SEND_FAIL(HttpStatus.BAD_REQUEST.value(), "이메일 전송에 실패하였습니다."),
    ALREADY_EXIST_LOGINID(HttpStatus.BAD_REQUEST.value(), "이미 사용중인 아이디 입니다."),
    PRECEED_SIGNUP(HttpStatus.BAD_REQUEST.value(), "회원 가입을 먼저 진행해주세요."),

    // server
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "내부 서버 오류가 발생했습니다."),


    // token
    EXPIRED_REFRESH_TOKEN(HttpStatus.FORBIDDEN.value(), "리프레시 토큰의 기간이 만료되었습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "리소스 접근 권한이 없습니다."),
    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED.value(), "토큰이 존재하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "유효하지 않는 토큰입니다."),
    WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED.value(), "잘못된 형식의 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "액세스 토큰이 만료되었습니다. 재발급 받아주세요."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "지원되지 않는 토큰입니다."),
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND.value(), "토큰을 찾을 수 없습니다.")
    ,TOKEN_NOT_MATCH(HttpStatus.BAD_REQUEST.value(), "토큰이 일치하지 않습니다."),

    // game
    GAME_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "게임을 찾을 수 없습니다."),
    FULL_PEOPLE_GAME(HttpStatus.BAD_REQUEST.value(),"신청 가능한 인원이 초과되어 더 이상 신청할 수 없습니다."),
    OVER_TIME_GAME(HttpStatus.BAD_REQUEST.value(), "신청 가능한 시간이 이미 지났습니다. 더 이상 신청할 수 없습니다."),
    ONLY_FEMALE_GAME(HttpStatus.BAD_REQUEST.value(), "여성만 신청 가능한 경기 입니다."),
    ONLY_MALE_GAME(HttpStatus.BAD_REQUEST.value(), "남성만 신청 가능한 경기 입니다."),
    ALREADY_PARTICIPANT_USER(HttpStatus.BAD_REQUEST.value(), "이미 신청한 유저입니다."),
    PARTICIPANT_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "참가한 게임을 찾을 수 없습니다."),
    CANCELLATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST.value(), "참가 취소가 불가능한 상태입니다."),

    ,ALREADY_REPORTED_USER(HttpStatus.BAD_REQUEST.value(), "이미 신고한 유저입니다.")

    // gameCreator


    // report
    ALREADY_REPORTED(HttpStatus.BAD_REQUEST.value(), "이미 신고한 유저입니다.");
    ;


    private final int httpStatus;
    private final String description;

}
