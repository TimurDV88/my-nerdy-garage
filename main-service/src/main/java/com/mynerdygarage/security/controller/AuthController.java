package com.mynerdygarage.security.controller;

import com.mynerdygarage.security.dto.LoginRequestDto;
import com.mynerdygarage.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDto loginRequestDto) {

        return authService.authenticateUser(loginRequestDto);
    }
}
