
package com.backend.paper3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProfileDto {

    private Long id;

    private String fullName;

    private String email;

    private String role;

    private Boolean isActive;
}

