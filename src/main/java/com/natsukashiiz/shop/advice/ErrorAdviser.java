package com.natsukashiiz.shop.advice;

import com.natsukashiiz.shop.exception.AuthException;
import com.natsukashiiz.shop.exception.BaseException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ErrorAdviser {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException e) {
        ErrorResponse response = new ErrorResponse();
        response.setError(e.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        ErrorResponse response = new ErrorResponse();
        response.setError(e.getMessage());
        response.setStatus(HttpStatus.EXPECTATION_FAILED.value());
        return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
    }

    @Data
    public static class ErrorResponse {
        private LocalDateTime timestamp = LocalDateTime.now();
        private int status;
        private String error;
    }
}
