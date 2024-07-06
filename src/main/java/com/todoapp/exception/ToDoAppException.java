package com.todoapp.exception;

import org.springframework.http.HttpStatus;

public abstract class ToDoAppException extends RuntimeException {

    protected abstract int getErrorCode();

    protected abstract String getErrorMessage();

    protected abstract HttpStatus getHttpStatus();

    @Override
    public String getMessage() {
        return String.format("ToDoAppException[%d]: %s", getErrorCode(), getErrorMessage());
    }

}
