package com.devmau.book.config;

import com.devmau.book.utils.ApiResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class HandlerError {

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ApiResponseError<String>> handleResourceNotFoundException(ResourceAccessException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponseConfig.error(
                        HttpStatus.NOT_FOUND.value(),
                        "Not found",
                        ex.getMessage()
                )
        );
    }
}
