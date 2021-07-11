package com.abnamro.nl.recipes.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

    private String error;
    private String message;

    public ErrorResponse(String error, String message) {
        super();
        this.error = error;
        this.message = message;
    }
}
