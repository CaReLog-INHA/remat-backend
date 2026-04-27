package com.remat.domain.auth.service;

import com.remat.domain.auth.dto.AuthReqDTO;
import com.remat.domain.auth.dto.AuthResDTO;
import com.remat.domain.auth.exception.AuthException;
import com.remat.domain.auth.exception.enums.AuthErrorCode;
import com.remat.domain.member.entity.Member;
import com.remat.domain.member.entity.Region;
import com.remat.domain.member.repository.MemberRepository;
import com.remat.global.auth.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public void signUp(AuthReqDTO.SignUpDTO reqDto) {
        if (!reqDto.password().equals(reqDto.passwordCheck())){
            throw new AuthException(AuthErrorCode.NOT_EQUAL_PASSWORD);
        }

        if (memberRepository.findByEmail(reqDto.email()).isPresent()) {
            throw new AuthException(AuthErrorCode.DUPLICATED_EMAIL);
        }

        Region region = Region.fromKoreanName(reqDto.regionName());

        Member member = Member.builder()
                .name(reqDto.name())
                .email(reqDto.email())
                .companyName(reqDto.companyName())
                .phoneNumber(reqDto.phoneNumber())
                .region(region)
                .password(passwordEncoder.encode(reqDto.password()))
                .build();

        memberRepository.save(member);
    }

    public AuthResDTO.LoginDTO login(AuthReqDTO.LoginDTO reqDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(reqDto.email(), reqDto.password())
            );
        } catch (AuthenticationException e) {
            throw new AuthException(AuthErrorCode.INVALID_LOGIN_CREDENTIALS);
        }

        String accessToken = jwtUtil.generateAccessToken(reqDto.email());
        String refreshToken = jwtUtil.generateRefreshToken(reqDto.email());

        return new AuthResDTO.LoginDTO(accessToken, refreshToken);
    }

    public AuthResDTO.RefreshTokenDTO refresh(AuthReqDTO.RefreshTokenDTO reqDto) {
        String refreshToken = reqDto.refreshToken();

        if (!jwtUtil.validateToken(refreshToken)|| !jwtUtil.isRefreshToken(refreshToken)) {
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        String email = jwtUtil.getEmailFromToken(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(email);

        return new AuthResDTO.RefreshTokenDTO(newAccessToken);
    }
}
