package com.example.basketballmatching.auth.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    @Value("${spring.jwt.header}")
    private String tokenHeader;

    @Value("${spring.jwt.prefix}")
    private String tokenPrefix;


    private final TokenProvider tokenProvider;

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = resolvedTokenFromRequest(request);

        if(StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {

            Authentication authentication = tokenProvider.getAuthentication(accessToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info(String.format("[%s] -> [%s]"), tokenProvider.getUsername(accessToken), request.getRequestURI());

        }

        filterChain.doFilter(request, response);

    }

    private String resolvedTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);

        if (!ObjectUtils.isEmpty(token) && token.startsWith(tokenPrefix)) {
            return token.substring(tokenPrefix.length());
        }
        return null;
    }
}
