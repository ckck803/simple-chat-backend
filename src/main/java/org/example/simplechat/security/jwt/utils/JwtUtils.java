package org.example.simplechat.security.jwt.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.security.dto.SessionUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    private SecretKey secretKey;

    @Value("${jwt.access_token.life_time}")
    private Long accessTokenLifeTime;

    @Value("${jwt.refresh_token.life_time}")
    private Long refreshTokenLifeTime;

    public JwtUtils(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public enum TokenType {
        ACCESS_TOKEN("ACCESS_TOKEN"), REFRESH_TOKEN("REFRESH_TOKEN");

        private String value;

        TokenType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }


    public String createAccessToken() {
        Date lifeTime = getLifeTime(TokenType.ACCESS_TOKEN.getValue());

        return createToken(lifeTime);
    }

    public String createRefreshToken() {
        Date lifeTime = getLifeTime(TokenType.REFRESH_TOKEN.getValue());

        return createToken(lifeTime);
    }

    public String getEmail(String token){
        Claims claims = this.getClaims(token);
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            this.getClaims(token);
            return true;
        } catch (SignatureException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다. Message : {}", e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다. Message : {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다. Message : {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다. Message : {}", e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Date getLifeTime(String tokenType) {
        if (tokenType == TokenType.ACCESS_TOKEN.getValue()) {
            return new Date(System.currentTimeMillis() + accessTokenLifeTime);
        } else {
            return new Date(System.currentTimeMillis() + refreshTokenLifeTime);
        }
    }

    private String createToken(Date lifeTime) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (!principal.getClass().isAssignableFrom(SessionUserInfo.class)) {
            return null;
        }

        SessionUserInfo memberInfo = (SessionUserInfo) principal;

        return Jwts.builder()
                .subject(memberInfo.getEmail())
                .signWith(secretKey)
                .compact();
    }
}
