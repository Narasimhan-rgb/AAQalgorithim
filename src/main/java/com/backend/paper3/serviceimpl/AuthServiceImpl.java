
package com.backend.paper3.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.paper3.algorithm.UserActivityAlgorithm;
import com.backend.paper3.dto.AuthDto;
import com.backend.paper3.dto.ProfileDto;
import com.backend.paper3.entity.UserEntity;
import com.backend.paper3.exception.ApiException;
import com.backend.paper3.mapper.AppMapper;
import com.backend.paper3.quantum.QuantumUserAnalyzer;
import com.backend.paper3.repository.UserRepository;
import com.backend.paper3.service.AuthService;
import com.backend.paper3.util.PasswordUtil;

@Service
public class AuthServiceImpl
        implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ProfileDto registerUser(
            AuthDto dto
    ) {

        if (dto.getEmail() == null
                || dto.getEmail().isEmpty()) {

            throw new ApiException(
                    "Email Cannot Be Empty"
            );
        }

        if (userRepository
                .findByEmail(dto.getEmail())
                .isPresent()) {

            throw new ApiException(
                    "Email Already Exists"
            );
        }

        double score =
                UserActivityAlgorithm
                        .calculateScore(10, 5);

        double quantumScore =
                QuantumUserAnalyzer
                        .calculateQuantumScore(
                                score
                        );

        System.out.println(
                quantumScore
        );

        UserEntity user =
                AppMapper.mapToUserEntity(dto);

        String encryptedPassword =
                PasswordUtil.encryptPassword(
                        dto.getPassword()
                );

        user.setPasswordHash(
                encryptedPassword
        );

        UserEntity savedUser =
                userRepository.save(user);

        return ProfileDto.builder()
                .id(savedUser.getId())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .isActive(savedUser.getIsActive())
                .build();
    }

    @Override
    public String login(
            AuthDto dto
    ) {

        UserEntity user =
                userRepository
                        .findByEmail(
                                dto.getEmail()
                        )
                        .orElseThrow(() ->
                                new ApiException(
                                        "Invalid Email"
                                )
                        );

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

        return "Login Successful";
    }
}

