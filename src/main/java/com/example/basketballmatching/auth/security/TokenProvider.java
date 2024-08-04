package com.example.basketballmatching.auth.security;


import com.example.basketballmatching.global.exception.CustomException;
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

import static com.example.basketballmatching.global.exception.ErrorCode.*;

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
        } catch (SecurityException | MalformedJwtException e) {
          log.error("Invalid JWT Token: {}" + e.getMessage());
          throw new CustomException(INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("JWT token is expired: {}", e.getMessage());
            throw new CustomException(EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("JWT token is unsupported", e.getMessage());
            throw new CustomException(UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT token is wrong type: {}", e.getMessage());
            throw new CustomException(WRONG_TYPE_TOKEN);
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
