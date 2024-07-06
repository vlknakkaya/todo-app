package com.todoapp.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ErrorResponse implements Serializable {

    private int errorCode;
    private HttpStatus httpStatus;
    private String message;
    private LocalDateTime timestamp;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponse that = (ErrorResponse) o;
        return getErrorCode() == that.getErrorCode();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getErrorCode());
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "errorCode=" + errorCode +
                ", httpStatus=" + httpStatus +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
