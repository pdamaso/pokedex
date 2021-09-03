package com.modyo.pokedex.infrastructure.adapter.rest.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;

@Getter
public class SourceApiServerError extends RuntimeException {
    private final HttpStatus statusCode;

    public SourceApiServerError(String message, HttpStatus statusCode, RestClientException cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}
