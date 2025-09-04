package com.melaniia.blog.controllers;

import com.melaniia.blog.domain.dtos.AuthResponse;
import com.melaniia.blog.domain.dtos.LoginRequest;
import com.melaniia.blog.domain.entities.User;
import com.melaniia.blog.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/auth/login")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService service;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        UserDetails userDetails = service.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        String tokenValue = service.generateToken(userDetails);
        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValue)
                .expiresIn(86400)
                .build();
        return ResponseEntity.ok(authResponse);
    }
}
