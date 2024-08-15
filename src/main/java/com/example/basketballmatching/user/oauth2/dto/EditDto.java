package com.example.basketballmatching.user.oauth2.dto;


import com.example.basketballmatching.user.dto.UserDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EditDto {


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        private String name;

        private String nickname;

        private String password;

        private String gender;

        private String position;


    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private Integer userId;

        private String loginId;

        private String email;

        private String name;

        private LocalDate birth;

        private String gender;

        private String nickname;

        private String position;

        private String userType;

        private LocalDateTime updatedAt;

        private String refreshToken;

        public static Response fromDto(UserDto userDto, String refreshToken) {
            return Response.builder()
                    .userId(userDto.getUserId())
                    .loginId(userDto.getLoginId())
                    .email(userDto.getEmail())
                    .name(userDto.getName())
                    .birth(userDto.getBirth())
                    .gender(userDto.getGenderType())
                    .nickname(userDto.getNickname())
                    .position(userDto.getPosition())
                    .userType(userDto.getUserType())
                    .updatedAt(userDto.getCreatedAt())
                    .refreshToken(refreshToken)
                    .build();
        }

    }

}
