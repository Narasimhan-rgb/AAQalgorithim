
package com.backend.paper3.response;

import java.util.List;

import com.backend.paper3.entity.UserEntity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ApiResponse<T> {

    private Boolean success;

    // actual response data
    private T results;

    // total error count
    private Integer errorCount;

    // list of errors
    private List<String> errors;
}

