package com.example.basketballmatching.auth.controller;

import com.example.basketballmatching.auth.dto.SignInDto;
import com.example.basketballmatching.auth.dto.SignUpDto;
import com.example.basketballmatching.auth.dto.TokenDto;
import com.example.basketballmatching.auth.service.AuthService;
import com.example.basketballmatching.global.dto.SendMailRequest;
import com.example.basketballmatching.global.dto.VerifyMailRequest;
import com.example.basketballmatching.global.service.MailService;
import com.example.basketballmatching.user.dto.UserDto;
import com.example.basketballmatching.user.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;

    private final MailService mailService;

    @PostMapping
    public ResponseEntity<?> signUpMember(@RequestBody SignUpDto request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(request));
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

        UserDto userDto = authService.signIn(request);

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

}
