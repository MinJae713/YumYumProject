package com.ssafy.yumyum.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final CustomUserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpirationMillis;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpirationMillis;

    private Key key;

    @PostConstruct
    public void init() {
        /**
         * secret 키를 byte[]로 변환 후 HMAC-SHA256 키 생성
         */
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Access Token 생성 (email + role 포함)
     */
    public String createAccessToken(String email, String role) {
        return buildToken(email, role, accessTokenExpirationMillis, true);
    }

    /**
     * Refresh Token 생성 (email만 포함)
     */
    public String createRefreshToken(String email) {
        return buildToken(email, null, refreshTokenExpirationMillis, false);
    }

    private String buildToken(String email, String role, long validityMillis, boolean includeRole) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityMillis);

        JwtBuilder builder = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256);

        if (includeRole && role != null) {
            builder.claim("role", role);
        }

        return builder.compact();
    }

    /**
     * JWT 유효성 체크
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;

        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰", e);
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT", e);
        } catch (MalformedJwtException e) {
            log.warn("잘못된 JWT 서명", e);
        } catch (IllegalArgumentException e) {
            log.warn("JWT 내용이 비어있음", e);
        } catch (SecurityException e) {
            log.warn("JWT 서명 검증 실패", e);
        }
        return false;
    }

    /**
     * 토큰 → email(subject)
     */
    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * 토큰 → Authentication (스프링 시큐리티에서 인증객체로 사용)
     */
    public Authentication getAuthentication(String token) {
        String email = getEmail(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    /**
     * 만료된 토큰도 claims는 꺼내도록 처리
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}

