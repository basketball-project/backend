package com.example.basketballmatching.auth.dto;


import com.example.basketballmatching.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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

    private String userType;

    private String genderType;

    private String position;



    public static SignUpDto fromEntity(User user) {

        return SignUpDto.builder()
                .loginId(user.getLoginId())
                .password(user.getPassword())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .userType(String.valueOf(user.getUserType()))
                .genderType(String.valueOf(user.getGenderType()))
                .position(String.valueOf(user.getPosition()))
                .build();
    }

}
