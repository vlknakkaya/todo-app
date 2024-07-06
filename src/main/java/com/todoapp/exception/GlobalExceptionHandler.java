package com.todoapp.exception;

import org.springframework.http.ResponseEntity;
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

}
