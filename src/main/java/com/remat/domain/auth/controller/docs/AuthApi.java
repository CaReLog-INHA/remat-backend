package com.remat.domain.auth.controller.docs;

import com.remat.domain.auth.dto.AuthReqDTO;
import com.remat.domain.auth.dto.AuthResDTO;
import com.remat.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 인증 API의 Swagger 문서 전용 인터페이스.
 */
@Tag(name = "인증", description = "회원가입 / 로그인 / 토큰 재발급 API")
public interface AuthApi {

    @Operation(
            summary = "회원가입",
            description = "신규 회원을 등록합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원가입 성공"),
    })
    ApiResponse<Void> signUp(AuthReqDTO.SignUpDTO reqDto);

    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호로 로그인하고 액세스 토큰과 리프레시 토큰을 발급합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
    })
    ApiResponse<AuthResDTO.LoginResDTO> login(AuthReqDTO.LoginDTO reqDto);

    @Operation(
            summary = "토큰 재발급",
            description = "리프레시 토큰으로 새로운 액세스 토큰을 발급합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "재발급 성공"),
    })
    ApiResponse<AuthResDTO.RefreshTokenResDTO> refresh(AuthReqDTO.RefreshTokenDTO reqDto);
}
