package com.todoapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ToDoAppException.class)
    public ResponseEntity<ErrorResponse> handleToDoAppException(ToDoAppException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(exception.getErrorCode());
        errorResponse.setHttpStatus(exception.getHttpStatus());
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(errorResponse.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(ErrorCodes.AUTHENTICATION_FAILED);
        errorResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(errorResponse.getHttpStatus()).body(errorResponse);
    }

}
