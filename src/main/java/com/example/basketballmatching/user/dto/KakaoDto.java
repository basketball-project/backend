package com.example.basketballmatching.user.dto;

import com.example.basketballmatching.user.entity.UserEntity;
import com.example.basketballmatching.user.type.GenderType;
import com.example.basketballmatching.user.type.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class KakaoDto {


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        private String loginId;

        private String email;

        private String name;

        private String nickname;

        private String gender;

        public static UserEntity toEntity(Request request) {

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            return UserEntity.builder()
                    .loginId(request.loginId)
                    .password(encoder.encode("kakao"))
                    .email(request.email)
                    .name(request.name)
                    .birth(LocalDate.now())
                    .genderType(GenderType.valueOf(request.gender))
                    .nickname(request.nickname)
                    .userType(UserType.USER)
                    .emailAuth(true)
                    .build();
        }

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private Integer id;

        private String loginId;

        private String email;

        @Schema(defaultValue = "방문자")
        private String name;

        @Schema(defaultValue = "19970724")
        private LocalDate birth;

        @Schema(defaultValue = "MALE")
        private String gender;

        private String nickname;

        private LocalDateTime createdAt;

        @Schema(defaultValue = "GUARD")
        private String position;

        private String userType;

        private String refreshToken;

        public static Response fromDto(UserDto userDto, String refreshToken) {

            return Response.builder()
                    .id(userDto.getUserId())
                    .loginId(userDto.getLoginId())
                    .email(userDto.getEmail())
                    .name(userDto.getName())
                    .birth(userDto.getBirth())
                    .gender(userDto.getGenderType())
                    .nickname(userDto.getNickname())
                    .createdAt(userDto.getCreatedAt())
                    .position(userDto.getPosition())
                    .userType(userDto.getUserType())
                    .refreshToken(refreshToken)
                    .build();

        }

    }

}
