package com.remat.domain.auth.dto;

public class AuthReqDTO {

    public record SignUpDTO(
            String name,
            String phoneNumber,
            String companyName,
            String email,
            String regionName, // 한글이름
            String password,
            String passwordCheck
    ){}

    public record LoginDTO(
            String email,
            String password
    ) {}

    public record RefreshTokenDTO(
            String refreshToken
    ) {}
}
