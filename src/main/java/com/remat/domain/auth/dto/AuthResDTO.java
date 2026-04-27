package com.remat.domain.auth.dto;

public class AuthResDTO {

    public record LoginDTO(
            String accessToken,
            String refreshToken
    ){}

    public record RefreshTokenDTO(
            String accessToken
    ){}
}
