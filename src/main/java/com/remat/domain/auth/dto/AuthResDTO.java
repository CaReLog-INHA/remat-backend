package com.remat.domain.auth.dto;

public class AuthResDTO {

    public record LoginResDTO(
            String accessToken,
            String refreshToken
    ){}

    public record RefreshTokenResDTO(
            String accessToken
    ){}
}
