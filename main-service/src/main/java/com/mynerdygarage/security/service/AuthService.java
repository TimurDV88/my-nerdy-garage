package com.mynerdygarage.security.service;

import com.mynerdygarage.security.dto.JwtResponseDto;
import com.mynerdygarage.security.dto.LoginRequestDto;
import com.mynerdygarage.security.jwt.JwtUtils;
import com.mynerdygarage.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;

    public ResponseEntity<?> authenticateUser(LoginRequestDto loginRequestDto) {

        log.info("-- Logging in by username={}", loginRequestDto.getUsername());

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequestDto.getUsername(), loginRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

        JwtResponseDto jwtResponseDto = new JwtResponseDto(user.getId(), user.getUsername(), user.getEmail());

        log.info("-- Logged in by user:{}", jwtResponseDto);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(jwtResponseDto);
    }
}
