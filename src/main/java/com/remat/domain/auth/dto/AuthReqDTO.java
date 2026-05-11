package com.remat.domain.auth.dto;

import com.remat.domain.member.entity.Region;

public class AuthReqDTO {

    public record SignUpDTO(
            String name,
            String phoneNumber,
            String companyName,
            String email,
            Region region,
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
