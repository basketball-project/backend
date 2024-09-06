package com.example.basketballmatching.auth.security;


import com.example.basketballmatching.admin.service.BlackListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final BlackListService blackListService;

    private final TokenProvider tokenProvider;

    private final ObjectMapper objectMapper;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = resolvedTokenFromRequest(request);

        try {


            if (StringUtils.hasText(accessToken) && tokenProvider.isLogout(accessToken)) {

                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");

                String errorMessage = objectMapper.writeValueAsString(
                        Map.of("error", "Unauthorized", "message",
                                "Your token is invalid"));

                response.getWriter().write(errorMessage);

            } else if (!StringUtils.hasText(accessToken)) {
                log.warn("Not have Access Token.");
            } else if (tokenProvider.validateToken(accessToken)) {
                Authentication authentication = tokenProvider.getAuthentication(accessToken);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info(String.format("[%s] -> %s", tokenProvider.getUsername(accessToken), request.getRequestURI()));

                try {

                    blackListService.checkBlackList(tokenProvider.getUsername(accessToken));

                } catch (Exception e) {
                    setUnauthorizedResponse(response, e.getMessage());
                    return;
                }

                log.info(String.format("[%s] -> %s",
                        tokenProvider.getUsername(accessToken),

                        request.getRequestURI()));

            }
        } catch (ExpiredJwtException e) {
            log.warn("에러 메세지 : " + e.getMessage());
        }

        filterChain.doFilter(request, response);

    }

    private String resolvedTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);

        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    private void setUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {


        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String errorMessage = objectMapper.writeValueAsString(
                Map.of("error", "Unauthorized", "message", message)
        );

        response.getWriter().write(errorMessage);

    }




}
