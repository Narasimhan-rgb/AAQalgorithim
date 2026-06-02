package com.backend.paper3.response;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Dataresponse<T> {

    private Boolean success;

    private T results;

    private Integer errorCount;

    private List<String> errors;
}