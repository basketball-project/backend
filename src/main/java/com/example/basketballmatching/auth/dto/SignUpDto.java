package com.example.basketballmatching.auth.dto;


import com.example.basketballmatching.user.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "휴대폰번호를 입력해주세요.")
    private String phone;

    private LocalDate birth;

    private String userType;

    private String genderType;

    private String position;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;



    public static SignUpDto fromEntity(UserEntity userEntity) {

        return SignUpDto.builder()
                .loginId(userEntity.getLoginId())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .phone(userEntity.getPhone())
                .birth(userEntity.getBirth())
                .userType(String.valueOf(userEntity.getUserType()))
                .genderType(String.valueOf(userEntity.getGenderType()))
                .position(String.valueOf(userEntity.getPosition()))
                .createdAt(userEntity.getCreatedAt())
                .deletedAt(userEntity.getUpdatedAt())
                .build();
    }

}
