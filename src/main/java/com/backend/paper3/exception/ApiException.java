
package com.backend.paper3.exception;

public class ApiException
        extends RuntimeException {

    public ApiException(
            String message
    ) {

        super(message);
    }
}

