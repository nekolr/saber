package com.nekolr.saber.security.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class TokenProvider {

    private final Duration period;
    private final SecretKey secretKey;

    public TokenProvider(@Value("${jwt.period}") Duration period) {
        this.period = period;
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String createToken(String username) {
        long now = new Date().getTime();
        Date expireDate = new Date(now + period.toMillis());

        return Jwts.builder()
                .setSubject(username)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .setId(UUID.randomUUID().toString().replaceAll("-", ""))
                .setExpiration(expireDate)
                .compact();
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // token 过期
            log.error("Token has expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            // token 格式错误
            log.error("Token format error: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            // token 构造错误
            log.error("Token construct error: {}", e.getMessage());
        } catch (SignatureException e) {
            // 签名失败
            log.error("Signature failed: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            // 非法参数
            log.error("Illegal argument: {}", e.getMessage());
        } catch (JwtException e) {
            // 其他异常
            log.error("Other exception: {}", e.getMessage());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            // token 过期
            log.error("Token has expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            // token 格式错误
            log.error("Token format error: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            // token 构造错误
            log.error("Token construct error: {}", e.getMessage());
        } catch (SignatureException e) {
            // 签名失败
            log.error("Signature failed: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            // 非法参数
            log.error("Illegal argument: {}", e.getMessage());
        } catch (JwtException e) {
            // 其他异常
            log.error("Other exception: {}", e.getMessage());
        }
        return false;
    }
}
