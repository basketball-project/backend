package com.example.basketballmatching.user.dto;

import com.example.basketballmatching.user.entity.UserEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Integer userId;

    private String loginId;

    private String password;

    private String email;

    private String name;

    private String nickname;

    private LocalDate birth;

    private String genderType;

    private String userType;

    private String position;

    private boolean emailAuth;

    private LocalDateTime createdAt;


    public static UserDto fromEntity(UserEntity userEntity) {

        return UserDto.builder()
                .userId(userEntity.getUserId())
                .loginId(userEntity.getLoginId())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .nickname(userEntity.getNickname())
                .birth(userEntity.getBirth())
                .genderType(String.valueOf(userEntity.getGenderType()))
                .userType(String.valueOf(userEntity.getUserType()))
                .position(String.valueOf(userEntity.getPosition()))
                .emailAuth(userEntity.isEmailAuth())
                .createdAt(userEntity.getCreatedAt())
                .build();
    }

}
