package com.example.basketballmatching.user.dto;

import com.example.basketballmatching.user.entity.User;
import lombok.*;

import java.time.LocalDate;


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

    private String phone;

    private LocalDate birth;

    private String genderType;

    private String userType;

    private String position;

    private boolean emailAuth;

    public static UserDto fromEntity(User user) {

        return UserDto.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .password(user.getPassword())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .birth(user.getBirth())
                .genderType(String.valueOf(user.getGenderType()))
                .userType(String.valueOf(user.getUserType()))
                .position(String.valueOf(user.getPosition()))
                .emailAuth(user.isEmailAuth())
                .build();
    }

}
