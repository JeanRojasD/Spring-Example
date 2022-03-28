package com.br.example.service;

import com.br.example.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class TokenService {

    private final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secret}")
    private String secret;

    private final UserService usersService;

    public TokenService(UserService usersService) {
        this.usersService = usersService;
    }

    public String generateToken(Authentication authentication) {
        User logged = (User) authentication.getPrincipal();

        Date today = new Date();

        Instant expirationTime = Instant.now()
                .plusSeconds(expiration);

        Date expirationDate = Date.from(expirationTime);

        Claims claims = Jwts.claims().setSubject(logged.getUsername());
        claims.put("roles", logged.getAuthorities());
        claims.put("userId", logged.getId());
        claims.put("username", logged.getUsername());

        return Jwts.builder()
                .setIssuer("Spring - Project")
                .setSubject(logged.getId().toString())
                .setIssuedAt(today)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .setClaims(claims)
                .compact();
    }

    public Boolean isValid(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            logger.error("[JWT] {}", e.getMessage());
            return false;
        }
    }

    public Long getUsersId(String token) {
        Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return Long.valueOf((Integer) body.get("userId"));
    }
}
