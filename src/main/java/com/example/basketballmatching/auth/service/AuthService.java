package com.example.basketballmatching.auth.service;


import com.example.basketballmatching.auth.dto.SignInDto;
import com.example.basketballmatching.auth.dto.SignUpDto;
import com.example.basketballmatching.auth.dto.TokenDto;
import com.example.basketballmatching.auth.security.TokenProvider;
import com.example.basketballmatching.global.exception.CustomException;
import com.example.basketballmatching.global.service.RedisService;
import com.example.basketballmatching.user.dto.DeleteUserDto;
import com.example.basketballmatching.user.dto.UserDto;
import com.example.basketballmatching.user.entity.UserEntity;
import com.example.basketballmatching.user.oauth2.dto.EditDto;
import com.example.basketballmatching.user.repository.UserRepository;
import com.example.basketballmatching.user.type.GenderType;
import com.example.basketballmatching.user.type.Position;
import com.example.basketballmatching.user.type.UserType;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.example.basketballmatching.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    public SignUpDto signUp(SignUpDto request) {
        if (userRepository.existsByEmailAndDeletedAtNull(request.getEmail())) {
            throw new CustomException(ALREADY_EXIST_USER);
        }

        if (userRepository.existsByLoginIdAndDeletedAtNull(request.getLoginId())) {
            throw new CustomException(ALREADY_EXIST_LOGINID);
        }


        UserEntity save = userRepository.save(
                UserEntity.builder()
                        .loginId(request.getLoginId())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .email(request.getEmail())
                        .name(request.getName())
                        .nickname(request.getNickname())
                        .birth(LocalDate.now())
                        .userType(UserType.USER)
                        .genderType(GenderType.valueOf(request.getGenderType()))
                        .position(Position.valueOf(request.getPosition()))
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        return SignUpDto.fromEntity(save);

    }

    public SignUpDto signUpManager(SignUpDto request) {
        if (userRepository.existsByEmailAndDeletedAtNull(request.getEmail())) {
            throw new CustomException(ALREADY_EXIST_USER);
        }

        if (userRepository.existsByLoginIdAndDeletedAtNull(request.getLoginId())) {
            throw new CustomException(ALREADY_EXIST_LOGINID);
        }


        UserEntity save = userRepository.save(
                UserEntity.builder()
                        .loginId(request.getLoginId())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .email(request.getEmail())
                        .name(request.getName())
                        .nickname(request.getNickname())
                        .birth(LocalDate.now())
                        .userType(UserType.ADMIN)
                        .genderType(GenderType.valueOf(request.getGenderType()))
                        .position(Position.valueOf(request.getPosition()))
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        return SignUpDto.fromEntity(save);

    }

    public UserDto LogInUser(SignInDto.Request request) {
        UserEntity user = userRepository.findByLoginIdAndDeletedAtNull(request.getLoginId())
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

    public TokenDto refreshToken(HttpServletRequest request, UserEntity userEntity) {

        String expiredAccessToken = validateAccessToken(request);

        String refreshToken = validateRefreshToken(request);

        Claims claims = tokenProvider.parseClaims(refreshToken);

        String loginId = claims.get("loginId", String.class);

        String email = claims.get("email", String.class);

        UserType userType = (UserType) claims.get("userType");

        if (!tokenMatch(expiredAccessToken, refreshToken)) {
            throw new CustomException(TOKEN_NOT_MATCH);
        }

        if (!userEntity.getLoginId().equals(loginId)) {
            throw new CustomException(INVALID_TOKEN);
        }

        try {
            redisService.findByLoginId(loginId);
        } catch (Exception e) {
            throw new CustomException(NOT_FOUND_TOKEN);
        }

        userRepository.findByLoginIdAndDeletedAtNull(loginId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        String accessToken = tokenProvider.createAccessToken(loginId, email, userType);

        return new TokenDto(loginId, accessToken, refreshToken);
    }

    public void logOut(HttpServletRequest request, UserEntity userEntity) {

        String accessToken = validateAccessToken(request);
        String refreshToken = validateRefreshToken(request);

        if(accessToken.isEmpty()) {
            throw new CustomException(INVALID_TOKEN);
        }

        Claims claims = tokenProvider.parseClaims(accessToken);
        String loginId = claims.getSubject();

        if (tokenMatch(accessToken, refreshToken) && loginId.equals(userEntity.getLoginId())) {
            redisService.deleteData(loginId);
        } else {
            throw new CustomException(INVALID_TOKEN);
        }

        tokenProvider.addToLogOutList(accessToken);

    }

    public UserDto getUserInfo(HttpServletRequest request, UserEntity userEntity) {

        isSameLoginId(request, userEntity);

        return UserDto.fromEntity(userEntity);
    }

    public UserDto editUserInfo(HttpServletRequest request, EditDto.Request editDto, UserEntity user) {

        isSameLoginId(request, user);

        if (editDto.getPassword() != null) {
            String encode = passwordEncoder.encode(editDto.getPassword());
            user.passwordEdit(encode);
        }

        user.edit(editDto);

        userRepository.save(user);

        return UserDto.fromEntity(user);

    }

    public void deleteUser(HttpServletRequest request, DeleteUserDto deleteUserDto, UserEntity user) {

        isSameLoginId(request, user);

        String accessToken = validateAccessToken(request);

        String refreshToken = validateRefreshToken(request);

        if (!tokenMatch(accessToken, refreshToken)) {
            throw new CustomException(INVALID_TOKEN);
        }

        LocalDateTime now = LocalDateTime.now();

        if (!user.getLoginId().equals(deleteUserDto.getLoginId())) {
            throw new CustomException(USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(deleteUserDto.getPassword(), user.getPassword())) {
            throw new CustomException(PASSWORD_NOT_MATCH);
        }

        logOut(request, user);


        user.setDeletedAt(now);

        userRepository.save(user);
    }

    private String validateAccessToken(HttpServletRequest request) {
        log.info("AccessToken 검증 시작");

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Authorization Header: {}", token); // 헤더 로그 출력

        if (!ObjectUtils.isEmpty(token) && token.startsWith("Bearer ")) {
            log.info("검증 완료");
            return token.substring("Bearer ".length());
        } else {
            log.error("AccessToken이 없거나 형식이 잘못되었습니다."); // 에러 로그 출력
            throw new CustomException(NOT_FOUND_TOKEN);
        }
    }

    private String validateRefreshToken(HttpServletRequest request) {
        log.info("RefreshToken 검증 시작");

        String token = request.getHeader("refreshToken");
        log.info("RefreshToken Header: {}", token); // 헤더 로그 출력

        if (!ObjectUtils.isEmpty(token) && token.startsWith("Bearer ")) {
            return token.substring("Bearer ".length());
        } else {
            log.error("RefreshToken이 없거나 형식이 잘못되었습니다."); // 에러 로그 출력
            throw new CustomException(NOT_FOUND_TOKEN);
        }
    }


    private boolean tokenMatch(String accessToken, String refreshToken) {

        Claims accessClaims = tokenProvider.parseClaims(accessToken);

        Claims refreshClaims = tokenProvider.parseClaims(refreshToken);

        String accessId = accessClaims.getSubject();
        String refreshId = refreshClaims.getSubject();

        return accessId.equals(refreshId);
    }

    private void isSameLoginId(HttpServletRequest request, UserEntity user) {
        String accessToken = validateAccessToken(request);

        Claims claims = tokenProvider.parseClaims(accessToken);

        String loginId = claims.getSubject();

        if (!user.getLoginId().equals(loginId)) {
            throw new CustomException(INVALID_TOKEN);
        }

    }

}
