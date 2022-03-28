package com.br.example.config.security.jwt;

import com.br.example.model.User;
import com.br.example.repository.UserRepository;
import com.br.example.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private TokenService tokenService;
    private UserRepository usersRepository;

    public JwtAuthenticationFilter(TokenService tokenService, UserRepository usersRepository) {
        this.tokenService = tokenService;
        this.usersRepository = usersRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromHeader(httpServletRequest);
        Boolean validToken = tokenService.isValid(token);
        if (validToken) {
            authenticateCredentials(token);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void authenticateCredentials(String token) {
        Long credentialsId = tokenService.getUsersId(token);
        User credential = usersRepository.findById(credentialsId).get();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(credential, null, credential.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String extractTokenFromHeader(HttpServletRequest httpServletRequest) {
        String header = httpServletRequest.getHeader("Authorization");
        if (header == null || header.isEmpty() || !header.startsWith("Bearer ")) {
            logger.error("[JWT] Missing JWT in request headers");
            return null;
        }
        return header.substring(7);
    }
}
