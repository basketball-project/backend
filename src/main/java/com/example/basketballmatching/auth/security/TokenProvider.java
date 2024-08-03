package com.example.basketballmatching.auth.security;


import com.example.basketballmatching.global.service.RedisService;
import com.example.basketballmatching.user.UserService;
import com.example.basketballmatching.user.type.UserType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.token.access-expiration-time}")
    private long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long REFRESH_TOKEN_EXPIRE_TIME;



    private final RedisService redisService;

    private final UserService userService;

    public String createAccessToken(String loginId, String email, UserType userType) {

        log.info("Access Token 생성 시작");

        return generateAccessToken(loginId, email, userType, ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String createRefreshToken(String loginId) {
        log.info("Refresh Token 생성 시작");
        String refreshToken = generateRefreshToken(loginId, REFRESH_TOKEN_EXPIRE_TIME);

        redisService.setDataExpire(loginId, refreshToken, REFRESH_TOKEN_EXPIRE_TIME);

        log.info("Refresh Token 저장 완료");
        return refreshToken;

    }


    public String generateAccessToken(String loginId, String email, UserType userType, Long expireTime) {
        Claims claims = Jwts.claims().setSubject(loginId);

        claims.put("email", email);
        claims.put("userType", userType);

        return returnToken(claims, expireTime);
    }

    public String generateRefreshToken(String loginId, Long expireTime) {
        Claims claims = Jwts.claims().setSubject(loginId);

        return returnToken(claims, expireTime);
    }

    private String returnToken(Claims claims, Long expireTime) {

        var now = new Date();
        var expireDate = new Date(now.getTime() + expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public static Key getSigningKey(byte[] secretKey) {
        return Keys.hmacShaKeyFor(secretKey);
    }

    public Claims parseClaims(String token) {

        try {

            return Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw new JwtException("토큰 인증 시간이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("지원하지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new JwtException("잘못된 토큰입니다.");
        } catch (MalformedJwtException e) {
            throw new JwtException("토큰 유형이 잘못되었습니다.");
        } catch (JwtException e) {
            throw new JwtException(e.getMessage());
        }
    }
    public String getUsername(String token) {
        return this.parseClaims(token).getSubject();
    }

    public Authentication getAuthentication(String jwt) {
        UserDetails userDetails = userService.loadUserByUsername(
                getUsername(jwt));

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );
    }






}
