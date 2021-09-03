package com.modyo.pokedex.infrastructure.presentation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError implements Serializable {

    private String path;
    private Instant timestamp;
    private String status;
    private String message;

    public static ApiError of(String path, HttpStatus statusCode, String message) {
        return ApiError.builder()
                .path(path)
                .timestamp(Instant.now())
                .status(statusCode.toString())
                .message(message)
                .build();
    }
}
