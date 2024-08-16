package com.example.basketballmatching.auth.config;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class JwtParserConfig {

    @Value("${spring.jwt.secret}")
    private String secretKey;
    @Bean
    public JwtParser jwtParser() {
        return Jwts.parserBuilder().setSigningKey(secretKey.getBytes(
                StandardCharsets.UTF_8)).build();
    }

}
