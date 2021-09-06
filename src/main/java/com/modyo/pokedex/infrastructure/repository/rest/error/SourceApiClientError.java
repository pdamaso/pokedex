package com.modyo.pokedex.infrastructure.repository.rest.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@Getter
public class SourceApiClientError extends RuntimeException {
    // class could extend from RetryableException to implement retry attempts somewhere
    private final HttpStatus statusCode;

    public SourceApiClientError(String message, HttpStatus statusCode, HttpClientErrorException cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}
