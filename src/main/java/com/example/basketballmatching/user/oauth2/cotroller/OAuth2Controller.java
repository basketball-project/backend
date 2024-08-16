package com.example.basketballmatching.user.oauth2.cotroller;


import com.example.basketballmatching.auth.dto.TokenDto;
import com.example.basketballmatching.auth.service.AuthService;
import com.example.basketballmatching.user.dto.KakaoDto;
import com.example.basketballmatching.user.dto.UserDto;
import com.example.basketballmatching.user.oauth2.service.OAuth2Service;
import com.nimbusds.oauth2.sdk.Response;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth2")
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;
    private final AuthService authService;


    /**
     * 카카오 로그인
     */

    @GetMapping("/login/kakao")
    public void getKakaoAuthUrl(HttpServletResponse response) throws IOException {
        response.sendRedirect(oAuth2Service.responseUrl());
    }

    @GetMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(
            @RequestParam(name = "code") String code)
            throws IOException {
        log.info("카카오 로그인 시작");
        UserDto userDto = oAuth2Service.kakaoLogin(code);
        log.info("토큰 요청");
        TokenDto tokenDto = authService.getToken(userDto);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", tokenDto.getAccessToken());

        log.info("카카오 로그인 완료");
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(KakaoDto.Response.fromDto(userDto, tokenDto.getRefreshToken()));
    }
}
