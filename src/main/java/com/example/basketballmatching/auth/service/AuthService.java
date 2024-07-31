package com.example.basketballmatching.auth.service;


import com.example.basketballmatching.auth.dto.SignUpDto;
import com.example.basketballmatching.global.exception.CustomException;
import com.example.basketballmatching.global.exception.ErrorCode;
import com.example.basketballmatching.user.entity.User;
import com.example.basketballmatching.user.repository.UserRepository;
import com.example.basketballmatching.user.type.GenderType;
import com.example.basketballmatching.user.type.Position;
import com.example.basketballmatching.user.type.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.basketballmatching.global.exception.ErrorCode.ALREADY_EXIST_USER;
import static com.example.basketballmatching.global.exception.ErrorCode.PASSWORD_NOT_MATCH;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public SignUpDto signUp(SignUpDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ALREADY_EXIST_USER);
        }


        User save = userRepository.save(
                User.builder()
                        .loginId(request.getLoginId())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .email(request.getEmail())
                        .name(request.getName())
                        .phone(request.getPhone())
                        .userType(UserType.USER)
                        .genderType(GenderType.valueOf(request.getGenderType()))
                        .position(Position.valueOf(request.getPosition()))
                        .build()
        );

        return SignUpDto.fromEntity(save);

    }

}
