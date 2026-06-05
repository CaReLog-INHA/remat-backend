package com.remat.domain.auth.controller;

import com.remat.domain.auth.dto.AuthReqDTO;
import com.remat.domain.auth.dto.AuthResDTO;
import com.remat.domain.auth.service.AuthService;
import com.remat.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<Void> signUp(
            @RequestBody AuthReqDTO.SignUpDTO reqDto
    ){
        authService.signUp(reqDto);
        return ApiResponse.ok();
    }

    @PostMapping("/login")
    public ApiResponse<AuthResDTO.LoginResDTO> login(
            @RequestBody AuthReqDTO.LoginDTO reqDto
    ){
        AuthResDTO.LoginResDTO resDto = authService.login(reqDto);
        return ApiResponse.ok(resDto);
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResDTO.RefreshTokenResDTO> refresh(
            @RequestBody AuthReqDTO.RefreshTokenDTO reqDto
    ){
        AuthResDTO.RefreshTokenResDTO resDto = authService.refresh(reqDto);
        return ApiResponse.ok(resDto);
    }
}
