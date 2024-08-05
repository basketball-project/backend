package com.example.basketballmatching.auth.dto;

import com.example.basketballmatching.user.dto.UserDto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;


public class SignInDto {



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "아아디를 입력하세요.")
        private String loginId;

        @NotBlank(message = "비밀번호를 입력하세요.")
        private String password;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private Integer userId;

        private String loginId;

        private String password;

        private String email;

        private String name;

        private String phone;

        private LocalDate birth;

        private String genderType;

        private String userType;

        private String position;

        private String refreshToken;

        public static Response fromDto(UserDto userDto, String refreshToken) {

            return Response.builder()
                    .userId(userDto.getUserId())
                    .loginId(userDto.getLoginId())
                    .password(userDto.getPassword())
                    .email(userDto.getEmail())
                    .name(userDto.getName())
                    .phone(userDto.getPhone())
                    .birth(userDto.getBirth())
                    .genderType(userDto.getGenderType())
                    .userType(userDto.getUserType())
                    .position(userDto.getPosition())
                    .refreshToken(refreshToken)
                    .build();

        }



    }





}
