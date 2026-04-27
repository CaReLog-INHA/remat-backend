package com.remat.global.auth.jwt;

import com.remat.global.auth.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessTokenValidTime;
    private final Long refreshTokenValidTime;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity}") Long accessTokenValidTime,
            @Value("${jwt.refresh-token-validity}") Long refreshTokenValidTime
    ){
        this.secretKey = Keys.hmacShaKeyFor((secret.getBytes(StandardCharsets.UTF_8)));
        this.accessTokenValidTime = accessTokenValidTime;
        this.refreshTokenValidTime = refreshTokenValidTime;
    }

    public String generateAccessToken(String email){
        return generateToken(email, accessTokenValidTime);
    }

    public String generateRefreshToken(String email){
        return generateToken(email, refreshTokenValidTime);
    }

    public String generateToken(String email, Long validityTime) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityTime);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public String getEmailFromToken(String token){
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            // 토큰 만료
            return false;
        } catch(JwtException e){
            // 토큰 손상 또는 잘못된 서명
            return false;
        }
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
