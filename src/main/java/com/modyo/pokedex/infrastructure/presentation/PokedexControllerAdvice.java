package com.modyo.pokedex.infrastructure.presentation;

import com.modyo.pokedex.infrastructure.repository.rest.error.SourceApiClientError;
import com.modyo.pokedex.infrastructure.repository.rest.error.SourceApiServerError;
import com.modyo.pokedex.infrastructure.presentation.model.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class PokedexControllerAdvice {

    @ExceptionHandler(value = {SourceApiClientError.class})
    public ResponseEntity<ApiError> handle(HttpServletRequest request, SourceApiClientError ex) {
        String path = request.getRequestURI();
        log.error("handleClientError, path={}, message={}", path, ex.getMessage());
        return buildErrorResponse(path, ex.getStatusCode(), ex.getMessage());
    }

    @ExceptionHandler(value = {SourceApiServerError.class})
    public ResponseEntity<ApiError> handle(HttpServletRequest request, SourceApiServerError ex) {
        String path = request.getRequestURI();
        log.error("handleServerError, path={}, message={}", path, ex.getMessage());
        return buildErrorResponse(path, ex.getStatusCode(), ex.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ApiError> handle(HttpServletRequest request, Exception ex) {
        String path = request.getRequestURI();
        log.error("handleServerError, path={}, message={}", path, ex.getMessage());
        return buildErrorResponse(path, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<ApiError> buildErrorResponse(String path, HttpStatus statusCode, String message) {
        ApiError apiError = ApiError.of(path, statusCode, message);
        return ResponseEntity.status(statusCode).body(apiError);
    }
}
