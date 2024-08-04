package com.example.basketballmatching.auth.service;


import com.example.basketballmatching.auth.dto.SignInDto;
import com.example.basketballmatching.auth.dto.SignUpDto;
import com.example.basketballmatching.auth.dto.TokenDto;
import com.example.basketballmatching.auth.security.TokenProvider;
import com.example.basketballmatching.global.exception.CustomException;
import com.example.basketballmatching.user.dto.UserDto;
import com.example.basketballmatching.user.entity.UserEntity;
import com.example.basketballmatching.user.repository.UserRepository;
import com.example.basketballmatching.user.type.GenderType;
import com.example.basketballmatching.user.type.Position;
import com.example.basketballmatching.user.type.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.basketballmatching.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public SignUpDto signUp(SignUpDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ALREADY_EXIST_USER);
        }


        UserEntity save = userRepository.save(
                UserEntity.builder()
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

    public UserDto signIn(SignInDto.Request request) {
        UserEntity user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(PASSWORD_NOT_MATCH);
        }

        if(!user.isEmailAuth()) {
            throw new CustomException(CONFIRM_EMAIL_AUTH);
        }

        return UserDto.fromEntity(user);
    }

    public TokenDto getToken(UserDto userDto) {
        String accessToken = tokenProvider.createAccessToken(userDto.getLoginId(), userDto.getEmail(), UserType.valueOf(userDto.getUserType()));

        String refreshToken = tokenProvider.createRefreshToken(userDto.getLoginId());

        return new TokenDto(userDto.getLoginId(), accessToken, refreshToken);

    }

}
