package com.example.basketballmatching.auth.controller;

import com.example.basketballmatching.admin.service.BlackListService;
import com.example.basketballmatching.auth.dto.SignInDto;
import com.example.basketballmatching.auth.dto.SignUpDto;
import com.example.basketballmatching.auth.dto.TokenDto;
import com.example.basketballmatching.auth.service.AuthService;
import com.example.basketballmatching.global.dto.SendMailRequest;
import com.example.basketballmatching.global.dto.VerifyMailRequest;
import com.example.basketballmatching.global.service.MailService;
import com.example.basketballmatching.user.dto.DeleteUserDto;
import com.example.basketballmatching.user.oauth2.dto.EditDto;
import com.example.basketballmatching.user.service.UserService;
import com.example.basketballmatching.user.dto.UserDto;
import com.example.basketballmatching.user.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;

    private final MailService mailService;

    private final UserService userService;

    private final BlackListService blackListService;

    @PostMapping
    public ResponseEntity<?> signUpMember(@RequestBody SignUpDto request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(request));
    }

    @PostMapping("/admin")
    public ResponseEntity<?> signUpAdmin(@RequestBody SignUpDto request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUpManager(request));
    }

    @PostMapping("/mail/certification")
    public ResponseEntity<?> sendCertificationMail(@RequestBody SendMailRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mailService.sendAuthMail(request.getEmail()));
    }

    @PostMapping("/mail/verify")
    public ResponseEntity<?> sendVerifyMail(@RequestBody VerifyMailRequest request) {
        mailService.verifyEmail(request.getEmail(), request.getCode());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(
            @RequestBody @Validated SignInDto.Request request
            ) {

        blackListService.checkBlackList(request.getLoginId());

        UserDto userDto = authService.LogInUser(request);

        TokenDto token = authService.getToken(userDto);

        HttpHeaders responseHeader = new HttpHeaders();

        responseHeader.set("Authorization", token.getAccessToken());


        return ResponseEntity.ok()
                .headers(responseHeader)
                .body(SignInDto.Response.fromDto(userDto,
                        token.getRefreshToken()));

    }

    @PostMapping("/logOut")
    public ResponseEntity<?> logOut(HttpServletRequest request, @AuthenticationPrincipal UserEntity userEntity) {
        authService.logOut(request, userEntity);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, @AuthenticationPrincipal UserEntity userEntity) {

        TokenDto tokenDto = authService.refreshToken(request, userEntity);

        UserDto userInfo = userService.getUserInfo(tokenDto.getLoginId());

        HttpHeaders responseHeader = new HttpHeaders();

        responseHeader.set("Authorization", tokenDto.getAccessToken());

        return ResponseEntity.ok()
                .headers(responseHeader)
                .body(SignInDto.Response.fromDto(userInfo, tokenDto.getRefreshToken()));
    }

    @GetMapping("/user/info")
    public ResponseEntity<UserDto> getUserInfo(
            HttpServletRequest request,
            @AuthenticationPrincipal UserEntity user
    ) {
        UserDto userInfo = authService.getUserInfo(request, user);

        return ResponseEntity.ok(userInfo);
    }

    @PatchMapping("/user/edit")
    public ResponseEntity<EditDto.Response> editUserInfo(
            HttpServletRequest request,
            @RequestBody @Validated EditDto.Request editDto,
            @AuthenticationPrincipal UserEntity user
    ) {
        UserDto userDto = authService.editUserInfo(request, editDto, user);

        TokenDto token = authService.getToken(userDto);

        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", token.getAccessToken());

        return ResponseEntity.ok()
                .headers(headers)
                .body(EditDto.Response.fromDto(userDto, token.getRefreshToken()));
    }

    @PostMapping("/delete")
    public ResponseEntity<HttpStatus> deleteUser(
            HttpServletRequest request,
            @RequestBody @Validated DeleteUserDto deleteUserDto,
            @AuthenticationPrincipal UserEntity user
    ) {
        authService.deleteUser(request, deleteUserDto, user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
