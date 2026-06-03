package com.backend.paper3.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.paper3.dto.AuthDto;
import com.backend.paper3.dto.LoginResponseDto;
import com.backend.paper3.dto.ProfileDto;
import com.backend.paper3.response.ApiResponse;
import com.backend.paper3.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ApiResponse<ProfileDto> registerUser(
            @RequestBody AuthDto dto
    ) {

        ProfileDto user =
                authService.registerUser(dto);

        return ApiResponse
                .<ProfileDto>builder()
                .success(true)
                .results(user)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(
            @RequestBody AuthDto dto
    ) {

        LoginResponseDto response =
                authService.login(dto);

        return ApiResponse
                .<LoginResponseDto>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }

    @GetMapping("/profile")
    public ApiResponse<ProfileDto> getProfile(
            @RequestHeader("Authorization") String authorizationHeader
    ) {

        ProfileDto profile =
                authService.getProfile(
                        authorizationHeader
                );

        return ApiResponse
                .<ProfileDto>builder()
                .success(true)
                .results(profile)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout() {

        String response =
                authService.logout();

        return ApiResponse
                .<String>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }
}