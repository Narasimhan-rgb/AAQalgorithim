
package com.backend.paper3.service;

import com.backend.paper3.dto.AuthDto;
import com.backend.paper3.dto.ProfileDto;

public interface AuthService {

    ProfileDto registerUser(
            AuthDto dto
    );

    String login(
            AuthDto dto
    );
}

