package com.backend.paper3.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.paper3.dto.AuthDto;
import com.backend.paper3.dto.LoginResponseDto;
import com.backend.paper3.dto.ProfileDto;
import com.backend.paper3.entity.UserEntity;
import com.backend.paper3.exception.ApiException;
import com.backend.paper3.mapper.AppMapper;
import com.backend.paper3.repository.UserRepository;
import com.backend.paper3.service.AuthService;
import com.backend.paper3.util.JwtUtil;
import com.backend.paper3.util.PasswordUtil;

@Service
public class AuthServiceImpl
        implements AuthService {

    private final UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ProfileDto registerUser(
            AuthDto dto
    ) {

        validateRegisterRequest(dto);

        String email =
                dto.getEmail()
                        .trim()
                        .toLowerCase();

        if (userRepository
                .findByEmail(email)
                .isPresent()) {

            throw new ApiException(
                    "Email Already Exists"
            );
        }

        dto.setEmail(email);

        if (dto.getRole() == null
                || dto.getRole().trim().isEmpty()) {

            dto.setRole("VIEWER");
        }

        UserEntity user =
                AppMapper.mapToUserEntity(dto);

        String encryptedPassword =
                PasswordUtil.encryptPassword(
                        dto.getPassword()
                );

        user.setPasswordHash(
                encryptedPassword
        );

        user.setIsActive(true);

        UserEntity savedUser =
                userRepository.save(user);

        return mapToProfileDto(savedUser);
    }

    @Override
    public LoginResponseDto login(
            AuthDto dto
    ) {

        validateLoginRequest(dto);

        String email =
                dto.getEmail()
                        .trim()
                        .toLowerCase();

        UserEntity user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () -> new ApiException(
                                        "Invalid Email"
                                )
                        );

        if (user.getIsActive() != null
                && !user.getIsActive()) {

            throw new ApiException(
                    "User account is inactive"
            );
        }

        String encryptedPassword =
                PasswordUtil.encryptPassword(
                        dto.getPassword()
                );

        if (!user.getPasswordHash()
                .equals(encryptedPassword)) {

            throw new ApiException(
                    "Invalid Password"
            );
        }

        String token =
                jwtUtil.generateToken(
                        user.getEmail()
                );

        return LoginResponseDto
                .builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public ProfileDto getProfile(
            String authorizationHeader
    ) {

        String token =
                jwtUtil.extractTokenFromHeader(
                        authorizationHeader
                );

        String email =
                jwtUtil.extractEmail(token);

        UserEntity user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () -> new ApiException(
                                        "User not found"
                                )
                        );

        return mapToProfileDto(user);
    }

    @Override
    public String logout() {

        return "Logout successful";
    }

    private void validateRegisterRequest(
            AuthDto dto
    ) {

        if (dto == null) {
            throw new ApiException(
                    "Request body is required"
            );
        }

        if (dto.getFullName() == null
                || dto.getFullName().trim().isEmpty()) {

            throw new ApiException(
                    "Full Name Cannot Be Empty"
            );
        }

        if (dto.getEmail() == null
                || dto.getEmail().trim().isEmpty()) {

            throw new ApiException(
                    "Email Cannot Be Empty"
            );
        }

        if (dto.getPassword() == null
                || dto.getPassword().trim().isEmpty()) {

            throw new ApiException(
                    "Password Cannot Be Empty"
            );
        }
    }

    private void validateLoginRequest(
            AuthDto dto
    ) {

        if (dto == null) {
            throw new ApiException(
                    "Request body is required"
            );
        }

        if (dto.getEmail() == null
                || dto.getEmail().trim().isEmpty()) {

            throw new ApiException(
                    "Email Cannot Be Empty"
            );
        }

        if (dto.getPassword() == null
                || dto.getPassword().trim().isEmpty()) {

            throw new ApiException(
                    "Password Cannot Be Empty"
            );
        }
    }

    private ProfileDto mapToProfileDto(
            UserEntity user
    ) {

        return ProfileDto
                .builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .build();
    }
}