
package com.backend.paper3.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.backend.paper3.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)

    public ResponseEntity<ApiResponse<Object>>
    handleApiException(
            ApiException ex
    ) {

        ApiResponse<Object> response =
                ApiResponse.builder()
                        .success(false)
                        .results(null)
                        .errorCount(1)
                        .errors(
                                List.of(
                                        ex.getMessage()
                                )
                        )
                        .build();

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }
}

