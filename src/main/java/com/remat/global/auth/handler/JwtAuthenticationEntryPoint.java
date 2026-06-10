package com.remat.global.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remat.global.code.CommonResponseCode;
import com.remat.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증되지 않은 요청(토큰 없음/만료/손상)이 보호 자원에 접근할 때 401을 반환한다.
 * 미설정 시 Spring 기본값은 403을 반환하므로, 프론트가 "재로그인"과 "권한 없음"을
 * 구분할 수 있도록 401을 명시한다.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(
                response.getWriter(),
                ApiResponse.error(CommonResponseCode.UNAUTHORIZED_ERROR)
        );
    }
}
